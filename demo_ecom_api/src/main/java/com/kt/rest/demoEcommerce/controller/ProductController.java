package com.kt.rest.demoEcommerce.controller;


import com.kt.rest.demoEcommerce.exeptions.ProductNotFoundException;
import com.kt.rest.demoEcommerce.model.entity.Product;
import com.kt.rest.demoEcommerce.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping

    public List<Product> getAllProducts(@RequestParam(name = "name_like", required = false, defaultValue = "") String name,
                                        @RequestParam(name = "featured", required = false, defaultValue = "") String featured) {
        if (!name.isEmpty()) {
            return productRepository.findAllByProductNameContainingIgnoreCase(name);
        } else if (featured.equals("true")) {
            return productRepository.findAllByFeaturedTrue();
        } else {
            return productRepository.findAll();
        }
    }

    @GetMapping(value = "/{id}")
    public Optional<Product> getOneProduct(@PathVariable Long id) {
        return Optional.ofNullable(productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id)));
    }


}
