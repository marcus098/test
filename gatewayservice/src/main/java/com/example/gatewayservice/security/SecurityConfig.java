package com.example.gatewayservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchange -> exchange
                        //.pathMatchers("/auth-service/auth/login", "/auth-service/auth/register").permitAll()
                        //.pathMatchers("/AUTH-SERVICE/auth/login", "/AUTH-SERVICE/auth/register").permitAll()
                        //.pathMatchers("/PRODUCT-SERVICE/products/shop/{userId}").authenticated()
                        //.pathMatchers("/PRODUCT-SERVICE/products/*").permitAll()
                        .anyExchange().permitAll()
                )
                .build();
    }
//
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//        return http
//                .csrf(csrf -> csrf.disable())  // questa è la disabilitazione corretta
//                .authorizeExchange(exchanges -> exchanges
//                        .pathMatchers("/AUTH-SERVICE/auth/login", "/AUTH-SERVICE/auth/register").permitAll()
//                        .anyExchange().authenticated()
//                )
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt())
//                .build();
//    }



    //@Bean
    //public ReactiveAuthenticationManager reactiveAuthenticationManager() {
    //    // qui implementa la tua logica di validazione JWT
    //    return authentication -> {
    //        // esempio dummy: autenticazione sempre fallita
    //        return Mono.error(new RuntimeException("Not implemented"));
    //    };
    //}
}
