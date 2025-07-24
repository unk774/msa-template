package ru.imageprocessing.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import ru.imageprocessing.gateway.feign.AuthClient;
import ru.imageprocessing.gateway.feign.StoreClient;
import ru.imageprocessing.gateway.feign.UserRegistryClient;

@SpringBootApplication
@EnableWebSecurity
@EnableFeignClients(clients = {AuthClient.class, StoreClient.class, UserRegistryClient.class})
//TODO reactive webflux
//TODO prometheus
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
