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

@SpringBootApplication
@EnableWebSecurity
@EnableFeignClients(clients = {AuthClient.class, StoreClient.class, UserRegistryClient.class, NotificationClient.class})
@Import({MDCConfiguration.class, RequestLoggingConfig.class, KeycloakConfiguration.class, OpenApiConfiguration.class})
//TODO user registry cache
//TODO user registry getByLogin checks
//TODO MDC, KeyCloak, S3, OpenApi вынести в commons
//TODO notification message broker chanel
//TODO reactive webflux
//TODO prometheus
//TODO tests + jacoco
//TODO js download button
//TODO large upload/download check
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
