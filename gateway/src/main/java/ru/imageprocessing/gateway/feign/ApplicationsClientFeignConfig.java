package ru.imageprocessing.gateway.feign;

import feign.RequestInterceptor;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationsClientFeignConfig {

    @Bean
    public RequestInterceptor bearerTokenRequestInterceptor() {
        return new BearerTokenInterceptor();
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(1000, 5000, 3);
    }
}