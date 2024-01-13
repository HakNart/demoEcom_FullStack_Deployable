package com.kt.rest.demoEcommerce.service;

import com.kt.rest.demoEcommerce.model.dto.ProductDTO;
import com.kt.rest.demoEcommerce.model.entity.Product;
import com.kt.rest.demoEcommerce.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDTO> findAllProductsByCriteria(String name, String featured) {
        List<Product> products;
        if (!name.isEmpty()) {
            products = productRepository.findAllByProductNameContainingIgnoreCase(name);
        } else if (featured.equals("true")) {
            products = productRepository.findAllByFeaturedTrue();
        } else {
            products = productRepository.findAll();
        }
        List<ProductDTO> productDTOList = new ArrayList<>();
        for (Product p: products) {
            productDTOList.add(mapProductToDTO(p));
        }
        return productDTOList;
    }

    private  ProductDTO mapProductToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .overview(product.getOverview())
                .longDescription(product.getLongDescription())
                .price(product.getPrice())
                .poster(product.getPoster())
                .imageLocal(product.getImageLocal())
                .rating(product.getRating())
                .inStock(product.getInStock())
                .bestSeller(product.getBestSeller())
                .featured(product.getFeatured())
                .build();
    }
}
