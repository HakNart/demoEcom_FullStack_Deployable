package com.kt.rest.demoEcommerce.controller;



import com.kt.rest.demoEcommerce.model.dto.ApiResponse;
import com.kt.rest.demoEcommerce.model.dto.ProductDTO;
import com.kt.rest.demoEcommerce.repository.ProductRepository;
import com.kt.rest.demoEcommerce.service.ProductService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private ProductRepository productRepository;
    private ProductService productService;

    public ProductController(ProductRepository productRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }

    @GetMapping

    public ResponseEntity<?> getAllProducts(@RequestParam(name = "name_like", required = false, defaultValue = "") String name,
                                         @RequestParam(name = "featured", required = false, defaultValue = "") String featured) {
        List<ProductDTO> productDTOList = productService.findAllProductsByCriteria(name, featured);
        ApiResponse apiResponse = ApiResponse.builder().success(productDTOList).build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getOneProduct(@PathVariable Integer id) {
        ApiResponse apiResponse = ApiResponse.builder().success(productService.findOneProductById(id)).build();
        return ResponseEntity.ok(apiResponse);
    }


}
