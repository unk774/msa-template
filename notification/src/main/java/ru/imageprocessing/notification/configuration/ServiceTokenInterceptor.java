package ru.imageprocessing.notification.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class ServiceTokenInterceptor implements RequestInterceptor {

    private final Keycloak keycloak;

    @Override
    public void apply(RequestTemplate template) {
        template.header("Authorization", "Bearer " + keycloak.tokenManager().getAccessToken().getToken());
    }
}
