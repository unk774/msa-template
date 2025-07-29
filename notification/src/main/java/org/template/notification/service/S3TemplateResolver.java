package org.template.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.cache.TTLCacheEntryValidity;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolution;
import org.thymeleaf.templateresource.ITemplateResource;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class S3TemplateResolver implements ITemplateResolver {

    private final S3Client s3Client;

    @RequiredArgsConstructor
    static class S3TemplateResource implements ITemplateResource {

        private final S3Client s3Client;
        private final String bucket;
        private final String objectKey;


        @Override
        public String getDescription() {
            return objectKey;
        }

        @Override
        public String getBaseName() {
            return bucket;
        }

        @Override
        public boolean exists() {
            try {
                s3Client.headObject(HeadObjectRequest.builder()
                        .bucket(bucket)
                        .key(objectKey)
                        .build());
                return true;
            } catch (NoSuchKeyException noSuchKeyException) {
                return false;
            }
        }

        @Override
        public Reader reader() {
            return new InputStreamReader(s3Client.getObject(GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(objectKey)
                    .build()));
        }

        @Override
        public ITemplateResource relative(String relativeLocation) {
            return null;
        }
    }

    @Value("${s3.templates-bucket}")
    private String templatesBucket;

    @Override
    public String getName() {
        return "S3 template resolver";
    }

    @Override
    public Integer getOrder() {
        return 0;
    }

    @Override
    public TemplateResolution resolveTemplate(IEngineConfiguration configuration, String ownerTemplate, String template, Map<String, Object> templateResolutionAttributes) {
        return new TemplateResolution(
                new S3TemplateResource(s3Client, templatesBucket, template),
                TemplateMode.TEXT,
                new TTLCacheEntryValidity(15000000)
        );
    }
}

