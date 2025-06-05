package com.example.productservice.configuration;

import com.example.productservice.resource.ProductResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(ProductResource.class); // oppure registra esplicitamente la classe
    }
}