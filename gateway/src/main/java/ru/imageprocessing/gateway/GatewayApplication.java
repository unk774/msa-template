package ru.imageprocessing.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import ru.imageprocessing.common.configuration.KeycloakConfiguration;
import ru.imageprocessing.common.configuration.OpenApiConfiguration;
import ru.imageprocessing.common.configuration.RequestLoggingConfig;
import ru.imageprocessing.gateway.feign.AuthClient;
import ru.imageprocessing.gateway.feign.NotificationClient;
import ru.imageprocessing.gateway.feign.StoreClient;
import ru.imageprocessing.gateway.feign.UserRegistryClient;
import ru.imageprocessing.common.configuration.MDCConfiguration;
import ru.imageprocessing.common.configuration.feign.FeignConfig;

@SpringBootApplication
@EnableWebSecurity
@EnableFeignClients(clients = {AuthClient.class, StoreClient.class, UserRegistryClient.class, NotificationClient.class})
@Import({MDCConfiguration.class, RequestLoggingConfig.class, KeycloakConfiguration.class,
        OpenApiConfiguration.class, FeignConfig.class})
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
