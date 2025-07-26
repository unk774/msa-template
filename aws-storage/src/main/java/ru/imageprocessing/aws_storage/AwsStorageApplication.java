package ru.imageprocessing.aws_storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import ru.imageprocessing.common.configuration.KeycloakConfiguration;
import ru.imageprocessing.common.configuration.MDCConfiguration;
import ru.imageprocessing.common.configuration.OpenApiConfiguration;
import ru.imageprocessing.common.configuration.RequestLoggingConfig;
import ru.imageprocessing.common.configuration.S3Configuration;

@SpringBootApplication
@EnableWebSecurity
@Import({MDCConfiguration.class, RequestLoggingConfig.class, KeycloakConfiguration.class,
        OpenApiConfiguration.class, S3Configuration.class})
public class AwsStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(AwsStorageApplication.class, args);
    }

}
