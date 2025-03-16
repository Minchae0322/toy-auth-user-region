package com.example.toyauth;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SecurityRequirement(name = "BearerAuth")
public class ToyAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToyAuthApplication.class, args);
    }

}
