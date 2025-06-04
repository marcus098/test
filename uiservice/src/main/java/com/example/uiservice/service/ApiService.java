package com.example.uiservice.service;

import com.example.commonlib.dto.ArticleDto;
import com.example.commonlib.dto.UserDto;
import com.example.commonlib.request.AddArticle;
import com.example.commonlib.response.AuthResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApiService {
    private final WebClient webClient;

    public ApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8070").build();
    }

    public List<ArticleDto> searchArticles(String name, String token) {
        return webClient.get()
                .uri("/PRODUCT-SERVICE/products/search?name={name}", name)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToFlux(ArticleDto.class)
                .collectList()
                .block();
    }

    public AuthResponse login(String username, String password) {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", username);
        credentials.put("password", password);

        return webClient.post()
                .uri("/AUTH-SERVICE/auth/login")
                .bodyValue(credentials)   // passiamo le credenziali nel body JSON
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .block();
    }

    public List<ArticleDto> getArticlesByShop(String token) {
        return webClient.get()
                .uri("/PRODUCT-SERVICE/products/myshop")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToFlux(ArticleDto.class)
                .collectList()
                .block();
    }

    public void createArticle(String token, AddArticle addArticle) {
        //ArticleDto dto = new ArticleDto(addArticle, userId);
        //String token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();

        webClient.post()
                .uri("/PRODUCT-SERVICE/products/shop")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(addArticle)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void deleteArticle(String articleId, String token) {
        webClient.delete()
                .uri("/PRODUCT-SERVICE/shops/delete/{id}", articleId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

}
