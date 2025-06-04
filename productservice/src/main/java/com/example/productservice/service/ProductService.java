package com.example.productservice.service;

import com.example.commonlib.dto.ArticleDto;
import com.example.commonlib.dto.Views;
import com.example.commonlib.model.GeoPoint;
import com.example.commonlib.security.JwtUtils;
import com.example.commonlib.utils.DistanceUtils;
import com.example.productservice.model.Article;
import com.example.productservice.repository.ProductRepository;
import com.example.commonlib.request.AddArticle;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.commonlib.utils.DistanceUtils.getLatitudeLongitude;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private JwtUtils jwtUtils;

    @JsonView(Views.Default.class)
    public List<ArticleDto> getAllProductsByShop() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getPrincipal().toString();
        List<Article> articles = productRepository.findByUserId(userId);
        return articles.stream()
                .map(article -> new ArticleDto(article.getId(), article.getName(), article.getCategory(), userId, article.getPrice(), article.getLocation().getX(), article.getLocation().getY()))
                .collect(Collectors.toList());
    }

    @JsonView(Views.Default.class)
    public ArticleDto createProduct(AddArticle addArticle) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getPrincipal().toString();
        Map<String, Double> latitudeLongitude = getLatitudeLongitude(authentication);
        Double latitude = latitudeLongitude.get("latitude");
        Double longitude = latitudeLongitude.get("longitude");

        Article article = new Article();
        article.setName(addArticle.getName());
        article.setCategory(addArticle.getCategory());
        article.setPrice(addArticle.getPrice());
        article.setUserId(userId);
        article.setLocation(new GeoJsonPoint(latitude, longitude));

        article = productRepository.save(article);

        return new ArticleDto(article.getId(), article.getName(), article.getCategory(), userId, article.getPrice(), article.getLocation().getX(), article.getLocation().getY());
    }

    @JsonView(Views.WithDistance.class)
    public List<ArticleDto> searchProduct(String name) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getPrincipal().toString();

        Map<String, Double> latitudeLongitude = getLatitudeLongitude(authentication);
        Double latitude = latitudeLongitude.get("latitude");
        Double longitude = latitudeLongitude.get("longitude");

        List<Article> articles = productRepository.findByNameNear(name, latitude, longitude);
        List<ArticleDto> result = new ArrayList<>();

        for (Article article : articles) {

            double distance = DistanceUtils.calculateDistance(
                    latitude, longitude,
                    article.getLocation().getX(), article.getLocation().getY()
            );
            ArticleDto dto = new ArticleDto(article.getId(), article.getName(), article.getCategory(), userId, article.getPrice(), article.getLocation().getX(), article.getLocation().getY(),distance);

            result.add(dto);
        }

        // ordina per distanza se vuoi
        result.sort(Comparator.comparing(ArticleDto::getDistance));
        return result;
    }
}
