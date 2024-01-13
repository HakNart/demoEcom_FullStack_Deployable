package com.kt.rest.demoEcommerce.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Integer id;

    private String productName;

    private String overview;

    private String longDescription;

    private BigDecimal price;

    private String poster;

    private String imageLocal;

    private Integer rating;

    private Boolean inStock;

    private Boolean bestSeller;

    private Boolean featured;
}
