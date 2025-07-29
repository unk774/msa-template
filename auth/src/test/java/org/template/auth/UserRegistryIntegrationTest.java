package org.template.auth;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.template.auth.service.Auth;
import org.template.auth.service.UserRegistry;
import org.template.auth.api.dto.LoginRequest;
import org.template.auth.api.dto.RegisterUserRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class UserRegistryIntegrationTest extends KeyCloakIntegrationTest{

    private final UserRegistry userRegistry;
    private final Auth auth;

    @Test
    void createAndCheck_IntegrationTest() {
        var createRequest = new RegisterUserRequest("test@none.com", "test", "password");
        createRequest.setFirstName("first");
        createRequest.setLastName("last");

        var resopnse = userRegistry.createUser(createRequest);
        assertNotNull(resopnse);

        var created = userRegistry.getUserByLogin("test");

        assertNotNull(created);
        assertEquals("test", created.getLogin());
        assertEquals("test@none.com", created.getEmail());
        assertEquals("first", created.getFirstName());
        assertEquals("last", created.getLastName());
        assertNotNull(created.getCreatedAt());

        assertNotNull(auth.login(new LoginRequest("test", "password")));
    }
}