package ru.imageprocessing.gateway.feign;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationsClientFeignConfig {

    /*используется токен principal*/
    @Bean
    public RequestInterceptor bearerTokenRequestInterceptor() {
        return new BearerTokenInterceptor();
    }
}