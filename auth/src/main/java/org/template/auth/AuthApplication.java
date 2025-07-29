package org.template.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.template.common.configuration.KeycloakConfiguration;
import org.template.common.configuration.MDCConfiguration;
import org.template.common.configuration.OpenApiConfiguration;
import org.template.common.configuration.RequestLoggingConfig;

@SpringBootApplication
@EnableWebSecurity
@EnableCaching
@Import({MDCConfiguration.class, RequestLoggingConfig.class, KeycloakConfiguration.class, OpenApiConfiguration.class})
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}
