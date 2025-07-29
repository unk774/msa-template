package org.template.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.template.common.configuration.KeycloakConfiguration;
import org.template.common.configuration.MDCConfiguration;
import org.template.common.configuration.OpenApiConfiguration;
import org.template.common.configuration.RequestLoggingConfig;
import org.template.common.configuration.S3Configuration;
import org.template.common.configuration.feign.FeignConfig;
import org.template.notification.feign.UserRegistryClient;

@SpringBootApplication
@EnableWebSecurity
@EnableFeignClients(clients = {UserRegistryClient.class})
@Import({MDCConfiguration.class, RequestLoggingConfig.class, KeycloakConfiguration.class,
        OpenApiConfiguration.class, S3Configuration.class, FeignConfig.class})
public class NotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }

}
