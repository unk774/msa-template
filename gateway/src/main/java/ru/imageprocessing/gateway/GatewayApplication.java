package ru.imageprocessing.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import ru.imageprocessing.gateway.feign.AuthClient;
import ru.imageprocessing.gateway.feign.NotificationClient;
import ru.imageprocessing.gateway.feign.StoreClient;
import ru.imageprocessing.gateway.feign.UserRegistryClient;

@SpringBootApplication
@EnableWebSecurity
@EnableFeignClients(clients = {AuthClient.class, StoreClient.class, UserRegistryClient.class, NotificationClient.class})
//TODO user registry cache
//TODO user registry getByLogin checks
//TODO MDC, KeyCloak, S3, OpenApi вынести в commons
//TODO notification message broker chanel
//TODO reactive webflux
//TODO prometheus
//TODO tests
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
