package ru.imageprocessing.notification.configuration.feign;

import feign.Feign;
import feign.Logger;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public Feign.Builder feignBuilder(ErrorDecoder errorDecoder) {
        return Feign.builder()
                .logLevel(Logger.Level.FULL)
                .doNotCloseAfterDecode()
                .errorDecoder(errorDecoder);
    }

    /*@Bean
    public RequestInterceptor bearerTokenRequestInterceptor() {
        return new ServiceTokenInterceptor();
    }*/

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(1000, 5000, 3);
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }

    @Bean
    public Decoder feignDecoder() {
        return new AutoClosingResponseEntityDecoder(new SpringDecoder(feignHttpMessageConverter()));
    }

    private ObjectFactory<HttpMessageConverters> feignHttpMessageConverter() {
        return () -> new HttpMessageConverters(
                new StreamingResponseBodyHttpMessageConverter()
        );
    }
}