package com.example.authservice.config;

import com.example.authservice.resources.AuthResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;


@Configuration
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(AuthResource.class); // oppure registra esplicitamente la classe
    }
}