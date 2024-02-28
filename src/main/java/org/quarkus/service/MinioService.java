package org.quarkus.service;

import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.quarkus.dto.FileDto;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.jboss.resteasy.cdi.i18n.LogMessages.LOGGER;

@ApplicationScoped
public class MinioService {
    @Inject
    MinioClient minioClient;

    public Response getBuckets() {
        try {
            List<String> bucketNames = minioClient.listBuckets()
                    .stream()
                    .map(Bucket::name)
                    .collect(Collectors.toList());

            return Response.ok(bucketNames).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error listing buckets: " + e.getMessage()).build();
        }
    }

    private String getFileExtension(String fileName) {
        Path path = Paths.get(fileName);
        String fileExtension = "";
        if (path != null) {
            fileExtension = path.getFileName().toString().substring(path.getFileName().toString().lastIndexOf('.') + 1);
        }
        return fileExtension.toLowerCase();
    }

    private boolean isImageFile(String fileExtension) {
        return "jpg".equalsIgnoreCase(fileExtension) || "jpeg".equalsIgnoreCase(fileExtension) || "png".equalsIgnoreCase(fileExtension);
    }
    public Response listAllFile(@PathParam("bucketName") String bucketName) {
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .includeVersions(true)
                            .recursive(true)
                            .build());
            List<FileDto> fileNames = new ArrayList<>();
            for (Result<Item> result : results) {
                Item item = result.get();
                String fileName = item.objectName();

                String presignedUrl = minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.GET)
                                .bucket(bucketName)
                                .object(fileName)
                                .expiry(60 * 100)
                                .build());

                // Extract file extension
                String fileExtension = getFileExtension(fileName);

                // Set content type based on file extension
                String contentType = "application/octet-stream"; // Default content type
                if ("pdf".equalsIgnoreCase(fileExtension)) {
                    contentType = "application/pdf";
                } else if ("xml".equalsIgnoreCase(fileExtension)) {
                    contentType = "application/xml";
                } else if (isImageFile(fileExtension)) {
                    contentType = "image/" + fileExtension;
                }

                fileNames.add(new FileDto(item.objectName(), item.lastModified().toString(), (int) item.size(), presignedUrl, contentType));
            }
            return Response.ok(fileNames).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error listing files in bucket: " + e.getMessage()).build();
        }
    }

    public Response createBucket(String bucketName) {
        try {
            MakeBucketArgs mbArgs = MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build();
            minioClient.makeBucket(mbArgs);

            BucketExistsArgs beArgs = BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build();

            if (minioClient.bucketExists(beArgs)) {
                LOGGER.info(bucketName + " exists.");
                return Response.ok("Bucket created successfully").build();
            } else {
                LOGGER.info(bucketName + " does not exist.");
                return Response.status(500).entity("Error creating bucket: " + bucketName).build();
            }
        } catch (Exception e) {
            LOGGER.error("Error creating bucket: " + e.getMessage());
            return Response.status(500).entity("Error creating bucket: " + e.getMessage()).build();
        }
    }

    public Response deleteBucket(String bucketName) {
        try {
            RemoveBucketArgs rbArgs = RemoveBucketArgs.builder()
                    .bucket(bucketName)
                    .build();
            minioClient.removeBucket(rbArgs);

            return Response.ok("Delete Bucket Success.").build();
        } catch (ErrorResponseException e) {
            if (e.errorResponse().equals("NoSuchBucket")) {
                LOGGER.info(bucketName + "does not exist.");
                return Response.status(404).entity("Bucket not found " + bucketName).build();
            } else {
                LOGGER.error("Error deleting bucket: " + e.getMessage());
                return Response.status(500).entity("Error deleting bucket: " + e.getMessage()).build();
            }
        } catch (Exception e) {
            LOGGER.error("Error deleting bucket: " + e.getMessage());
            return Response.status(500).entity("Error deleting bucket: " + e.getMessage()).build();
        }
    }

    public Response uploadFile(String bucketName, InputStream fileStream, String fileName) {
        try {
            PutObjectArgs uArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(fileStream, -1, 15695123)
                    .contentType("application/pdf")
                    .build();

            // Upload the file to MinIO
            ObjectWriteResponse resp = minioClient.putObject(uArgs);

            if (resp != null && resp.etag() != null) {
                // File uploaded successfully
                return Response.ok("File uploaded successfully!").build();
            } else {
                // File upload failed
                return Response.status(500).entity("Error uploading file: Empty or invalid response from MinIO").build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Error uploading file: " + e.getMessage()).build();
        }
        finally {
            try {
                // Close the InputStream
                fileStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Response deleteFile(String bucketName, String fileName) {
        try {
            RemoveObjectArgs rArgs = RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build();

            minioClient.removeObject(rArgs);
            return Response.ok("File deleted successfully").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Error deleting file: " + e.getMessage()).build();
        }
    }

    public InputStream dowloadFile(String bucketName, String fileName) {
        try {
            return minioClient.getObject(bucketName, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Response editFileName(String bucketName, String oldFileName, String newFileName) {
        try {
            minioClient.copyObject(CopyObjectArgs.builder()
                    .source(CopySource.builder().bucket(bucketName).object(oldFileName).build())
                    .bucket(bucketName)
                    .object(newFileName)
                    .build());

            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(oldFileName)
                    .build());

            return Response.ok("File edited successfully!").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("Error editing file: " + e.getMessage()).build();
        }
    }

//    public Response getStatObject(String bucketName, String fileName) {
//        try {
//            ObjectStat objectStat = minioClient.statObject(StatObjectArgs.builder()
//                    .bucket(bucketName)
//                    .object(fileName)
//                    .build());
//
//            Map<String, Object> statMap = new HashMap<>();
//            statMap.put("length", objectStat.length());
//            statMap.put("creatAt", objectStat.createdTime());
//
//            return Response.ok(statMap).build();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}