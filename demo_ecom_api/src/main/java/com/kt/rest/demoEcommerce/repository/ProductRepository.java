package com.kt.rest.demoEcommerce.repository;

import com.kt.rest.demoEcommerce.model.entity.Product;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@CacheConfig(cacheNames = "productsCache")
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Cacheable
    List<Product> findAllByNameContainingIgnoreCase(String name);
    @Cacheable
    List<Product> findAllByFeaturedTrue();
}
