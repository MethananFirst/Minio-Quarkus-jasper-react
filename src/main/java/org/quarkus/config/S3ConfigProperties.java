package org.quarkus.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "minio.client")
public interface S3ConfigProperties {
    String url();

    String accessKey();

    String secretPass();

    String bucket();
}
