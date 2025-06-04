package com.example.productservice.configuration;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import static org.springframework.core.annotation.AnnotationFilter.packages;

@Configuration
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        packages("com.example.productservice"); // oppure registra esplicitamente la classe
    }
}