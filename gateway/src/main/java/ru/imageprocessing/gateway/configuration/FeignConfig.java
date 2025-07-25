package ru.imageprocessing.gateway.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Response;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.ServerErrorException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import static feign.FeignException.errorStatus;

@Configuration
public class FeignConfig {

    public static class FeignErrorDecoder implements ErrorDecoder {

        @Override
        public Exception decode(String methodKey, Response response) {
            if (response.status() >= 400 && response.status() <= 499) {
                return new ClientErrorException(
                        response.status()
                );
            }
            if (response.status() >= 500 && response.status() <= 599) {
                return new ServerErrorException(
                        response.status()
                );
            }
            return errorStatus(methodKey, response);
        }
    }

    public static class BearerTokenInterceptor implements RequestInterceptor {

        @Override
        public void apply(RequestTemplate template) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.getCredentials() instanceof Jwt) {
                Jwt jwt = (Jwt) authentication.getCredentials();
                template.header("Authorization", "Bearer " + jwt.getTokenValue());
            }
        }
    }

    @Bean
    public RequestInterceptor bearerTokenRequestInterceptor() {
        return new BearerTokenInterceptor();
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(1000, 5000, 3);
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }
}