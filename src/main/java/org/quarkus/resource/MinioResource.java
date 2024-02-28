package org.quarkus.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.quarkus.service.FileService;
import org.quarkus.service.MinioService;


import java.io.InputStream;


@Path("/minio")
public class MinioResource {

    @Inject
    MinioService minioService;

    @GET
    @Path("/listAllBuckets")
    @Produces(MediaType.APPLICATION_JSON)
    @Schema(implementation = MinioService.class)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "ok" , content = @Content(mediaType = "application/json"))
    })
    public Response getBuckets() {
        return minioService.getBuckets();
    }

    @GET
    @Path("/listAllFile/{bucketName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAllFile(@PathParam("bucketName") String bucketName) {
        return minioService.listAllFile(bucketName);
    }

    @POST
    @Path("/createBucket/{bucketName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBucket(@PathParam("bucketName") String bucketName) {
        return minioService.createBucket(bucketName);
    }

    @DELETE
    @Path("/deleteBucket/{bucketName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBucket(@PathParam("bucketName") String bucketName) {
        return minioService.deleteBucket(bucketName);
    }

    @POST
    @Path("/uploadFile/{bucketName}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadBucket(@PathParam("bucketName") String bucketName,
                                 @MultipartForm FileService fileService) {
        InputStream stream = fileService.file;
        return minioService.uploadFile(bucketName, stream , fileService.fileName);
    }

    @DELETE
    @Path("/{bucketName}/{fileName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFile(@PathParam("bucketName") String bucketName,
                               @PathParam("fileName") String fileName) {
        return minioService.deleteFile(bucketName, fileName);
    }

    @GET
    @Path("/download/{bucketName}/{fileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFile(@PathParam("bucketName") String bucketName,
                                 @PathParam("fileName") String fileName) {
        InputStream file = minioService.dowloadFile(bucketName, fileName);
        return Response.ok(file).entity(minioService.dowloadFile(bucketName, fileName)).build();
    }

    @PUT
    @Path("/{bucketName}/{oldFileName}/{newFileName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editFileName(@PathParam("bucketName") String bucketName,
                                 @PathParam("oldFileName") String oldFileName,
                                 @PathParam("newFileName") String newFileName) {
        return minioService.editFileName(bucketName, oldFileName, newFileName);
    }

//    @GET
//    @Path("/getStat/{bucketName}/{fileName}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getStatBucket(@PathParam("bucketName") String bucketName,
//                                  @PathParam("fileName") String fileName) {
//        return minioService.getStatObject(bucketName, fileName);
//    }
}
