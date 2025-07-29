package org.template.aws_storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.template.common.configuration.KeycloakConfiguration;
import org.template.common.configuration.MDCConfiguration;
import org.template.common.configuration.OpenApiConfiguration;
import org.template.common.configuration.RequestLoggingConfig;
import org.template.common.configuration.S3Configuration;

@SpringBootApplication
@EnableWebSecurity
@Import({MDCConfiguration.class, RequestLoggingConfig.class, KeycloakConfiguration.class,
        OpenApiConfiguration.class, S3Configuration.class})
public class AwsStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(AwsStorageApplication.class, args);
    }

}
