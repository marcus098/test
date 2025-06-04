package com.example.commonlib.dto;

import com.example.commonlib.request.AddArticle;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {
    @JsonView(Views.Default.class)
    private String id;
    @JsonView(Views.Default.class)
    private String name;
    @JsonView(Views.Default.class)
    private String category;
    @JsonView(Views.Default.class)
    private String shopId;
    @JsonView(Views.Default.class)
    private double price;
    @JsonView(Views.Default.class)
    private Double latitude;
    @JsonView(Views.Default.class)
    private Double longitude;
    @JsonView(Views.WithDistance.class)
    private Double distance;

    public ArticleDto(AddArticle addArticle, String shopId){
        this.name = addArticle.getName();
        this.category = addArticle.getCategory();
        this.price = addArticle.getPrice();
        this.shopId = shopId;
    }

    public ArticleDto(String id, String name, String category, String shopId, double price){
        this.id = id;
        this.name = name;
        this.category = category;
        this.shopId = shopId;
        this.price = price;
    }

    public ArticleDto(String id, String name, String category, String shopId, double price, Double latitude, Double longitude){
        this.id = id;
        this.name = name;
        this.category = category;
        this.shopId = shopId;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
