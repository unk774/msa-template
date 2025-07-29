package org.template.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.template.common.configuration.KeycloakConfiguration;
import org.template.common.configuration.OpenApiConfiguration;
import org.template.common.configuration.RequestLoggingConfig;
import org.template.gateway.feign.AuthClient;
import org.template.gateway.feign.NotificationClient;
import org.template.gateway.feign.StoreClient;
import org.template.gateway.feign.UserRegistryClient;
import org.template.common.configuration.MDCConfiguration;
import org.template.common.configuration.feign.FeignConfig;

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
