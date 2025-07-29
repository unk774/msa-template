package org.template.common.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
public class S3Configuration {

    @Value("${s3.access.key:}")
    private String accessKey;

    @Value("${s3.secret.key:}")
    private String secretKey;

    @Value("${s3.service.endpoint:}")
    private String serviceEndpoint;

    @Value("${s3.signing.region:}")
    private String signingRegion;

    @Value("${s3.forcePathStyle:false}")
    private boolean forcePathStyle;

    @Bean
    public S3Client s3Client() {
        String endpoint = serviceEndpoint;
        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .endpointOverride(URI.create(endpoint))
                .region(Region.of(signingRegion))
                .forcePathStyle(forcePathStyle)
                .build();
    }

    @Bean
    public S3Presigner s3PreSigner(S3Client s3Client) {
        return S3Presigner.builder()
                .region(s3Client.serviceClientConfiguration().region())
                .endpointOverride(s3Client.serviceClientConfiguration().endpointOverride().get())
                .serviceConfiguration(software.amazon.awssdk.services.s3.S3Configuration.builder()
                        .pathStyleAccessEnabled(forcePathStyle) // Required for MinIO
                        .build())
                .credentialsProvider(s3Client.serviceClientConfiguration().credentialsProvider())
                .build();
    }
}
