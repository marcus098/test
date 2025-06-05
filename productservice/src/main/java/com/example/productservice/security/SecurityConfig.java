package com.example.productservice.security;

import com.example.commonlib.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher; // Importa questa classe
import org.springframework.http.HttpMethod; // Importa HttpMethod se vuoi specificare i metodi HTTP

@Configuration
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disabilita CSRF per API REST stateless
                .authorizeHttpRequests(auth -> auth
                        // Utilizzo di AntPathRequestMatcher per definire i pattern URL
                        // Ho unificato le due regole per /products/shop con un pattern più generico
                        .requestMatchers(new AntPathRequestMatcher("/products/myshop")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/products/shop")).hasRole("ADMIN") // Per il POST
                        .requestMatchers(new AntPathRequestMatcher("/products/shop/**", HttpMethod.DELETE.name())).hasRole("ADMIN") // Per il DELETE con ID o altri sub-path
                        .requestMatchers(new AntPathRequestMatcher("/products/search")).hasRole("USER")
                        .anyRequest().authenticated() // Tutte le altre richieste devono essere autenticate
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Aggiunge il filtro JWT personalizzato

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}