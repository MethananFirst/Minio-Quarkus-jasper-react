package org.quarkus.config;

import io.minio.MinioClient;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Produces;
import org.jboss.logging.Logger;

public class S3Configuration {
    private static final Logger LOGGER = Logger.getLogger("ListenerBean");

    @Produces
    @Singleton
    public MinioClient minioClient(S3ConfigProperties properties){
        LOGGER.info("minio url...:"+properties.url());

        return MinioClient.builder()
                .endpoint(properties.url())
                .credentials(properties.accessKey(), properties.secretPass())
                .build();
    }
}
