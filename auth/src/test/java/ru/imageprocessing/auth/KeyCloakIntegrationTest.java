package ru.imageprocessing.auth;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers(disabledWithoutDocker = true)
public abstract class KeyCloakIntegrationTest {

    protected static KeycloakContainer keycloak = new KeycloakContainer()
            .withRealmImportFile("/realm.json")
            .withReuse(true);

    static {
        keycloak.start();
    }

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("keycloak.auth-server-url", () -> keycloak.getAuthServerUrl());
    }
}
