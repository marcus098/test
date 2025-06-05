package com.example.productservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;

@SpringBootApplication(scanBasePackages = {
        "com.example.productservice",         // tuo package locale
        "com.example.commonlib"           // dove si trova JwtAuthenticationFilter e JwtUtils
}, exclude = {WebMvcAutoConfiguration.class})
public class ProductServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
}