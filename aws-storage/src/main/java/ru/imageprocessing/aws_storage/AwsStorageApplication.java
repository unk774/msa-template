package ru.imageprocessing.aws_storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class AwsStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(AwsStorageApplication.class, args);
    }

}
