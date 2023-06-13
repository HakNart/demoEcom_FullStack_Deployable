package com.kt.rest.demoEcommerce.models.shopEntities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name="product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String overview;

    @Column(name = "long_description", length = 2048)
    @JsonProperty("long_description")
    private String longDescription;

    private BigDecimal price;

    private String poster;

    @Column(name = "image_local")
    @JsonProperty("image_local")
    private String imageLocal;

    private Integer rating;

    @Column(name = "in_stock")
    @JsonProperty("in_stock")
    private Boolean inStock;

    @Column(name = "best_seller")
    @JsonProperty("best_seller")
    private Boolean bestSeller;

    @Column(name = "featured")
    private Boolean featured;

    public Product(String name, String overview, String longDescription, BigDecimal price, String poster, String imageLocal, Integer rating, Boolean inStock, Boolean bestSeller, Boolean featured) {
        this.name = name;
        this.overview = overview;
        this.longDescription = longDescription;
        this.price = price;
        this.poster = poster;
        this.imageLocal = imageLocal;
        this.rating = rating;
        this.inStock = inStock;
        this.bestSeller = bestSeller;
        this.featured = featured;
    }

    public Product() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getImageLocal() {
        return imageLocal;
    }

    public void setImageLocal(String imageLocal) {
        this.imageLocal = imageLocal;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    public Boolean getBestSeller() {
        return bestSeller;
    }

    public void setBestSeller(Boolean bestSeller) {
        this.bestSeller = bestSeller;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", overview='" + overview + '\'' +
                ", longDescription='" + longDescription + '\'' +
                ", price=" + price +
                ", poster='" + poster + '\'' +
                ", imageLocal='" + imageLocal + '\'' +
                ", rating=" + rating +
                ", inStock=" + inStock +
                ", bestSeller=" + bestSeller +
                ", featured=" + featured +
                '}';
    }
}
