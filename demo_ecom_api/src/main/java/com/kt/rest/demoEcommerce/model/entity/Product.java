package com.kt.rest.demoEcommerce.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name="product")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "product_name")
    private String productName;

    private String overview;

    @Column(name = "long_description", length = 2048)
    private String longDescription;

    private BigDecimal price;

    private String poster;

    @Column(name = "image_local")
    private String imageLocal;

    private Integer rating;

    @Column(name = "in_stock")
    private Boolean inStock;

    @Column(name = "best_seller")
    private Boolean bestSeller;

    @Column(name = "featured")
    private Boolean featured;

}
