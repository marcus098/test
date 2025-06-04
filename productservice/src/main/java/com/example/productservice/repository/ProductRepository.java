package com.example.productservice.repository;

import com.example.commonlib.dto.ArticleDto;
import com.example.productservice.model.Article;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProductRepository extends MongoRepository<Article, String> {
    List<Article> findByUserId(String userId);

    @Query("{ 'name': { $regex: ?0, $options: 'i' }, 'location': { $near: { $geometry: { type: 'Point', coordinates: [?2, ?1] } } } }")
    List<Article> findByNameNear(String name, double lat, double lon);
}
