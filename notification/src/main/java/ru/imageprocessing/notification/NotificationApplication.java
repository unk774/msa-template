package ru.imageprocessing.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import ru.imageprocessing.common.configuration.KeycloakConfiguration;
import ru.imageprocessing.common.configuration.MDCConfiguration;
import ru.imageprocessing.common.configuration.OpenApiConfiguration;
import ru.imageprocessing.common.configuration.RequestLoggingConfig;
import ru.imageprocessing.common.configuration.S3Configuration;
import ru.imageprocessing.notification.feign.UserRegistryClient;

@SpringBootApplication
@EnableWebSecurity
@EnableFeignClients(clients = {UserRegistryClient.class})
@Import({MDCConfiguration.class, RequestLoggingConfig.class, KeycloakConfiguration.class,
        OpenApiConfiguration.class, S3Configuration.class})
public class NotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }

}
