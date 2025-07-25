package ru.imageprocessing.gateway.configuration;

import feign.RequestInterceptor;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.imageprocessing.gateway.feign.BearerTokenInterceptor;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor bearerTokenRequestInterceptor() {
        return new BearerTokenInterceptor();
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(1000, 5000, 3);
    }
}