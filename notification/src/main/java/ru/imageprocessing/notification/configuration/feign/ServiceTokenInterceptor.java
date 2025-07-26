package ru.imageprocessing.notification.configuration.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceTokenInterceptor implements RequestInterceptor {

    private final Keycloak keycloak;

    @Override
    public void apply(RequestTemplate template) {
        template.header("Authorization", "Bearer " + keycloak.tokenManager().getAccessToken().getToken());
    }
}
