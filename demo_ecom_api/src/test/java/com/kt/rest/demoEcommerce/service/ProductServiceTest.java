package com.kt.rest.demoEcommerce.service;

import com.kt.rest.demoEcommerce.config.AppUnitTest;
import com.kt.rest.demoEcommerce.exeptions.ResourceNotFoundException;
import com.kt.rest.demoEcommerce.model.dto.ProductDTO;
import com.kt.rest.demoEcommerce.model.entity.Product;
import com.kt.rest.demoEcommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@AppUnitTest
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    private List<Product> products;

    @BeforeEach
    void setUp() {

        productService = new ProductService(productRepository);
        products = new ArrayList<>();
        products.add(new Product(1, "Product 1", "Overview 1", "Description 1", BigDecimal.valueOf(10.0), "poster1.jpg", "image1.jpg", 4, true, true, true));
    }

    @Test
    void testFindAllProductsByCriteria_ByName() {
        when(productRepository.findAllByProductNameContainingIgnoreCase("Product")).thenReturn(products);

        List<ProductDTO> result = productService.findAllProductsByCriteria("Product", null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Product 1", result.get(0).getProductName());
    }

    @Test
    void testFindAllProductsByCriteria_ByFeatured() {
        when(productRepository.findAllByFeaturedTrue()).thenReturn(products);

        List<ProductDTO> result = productService.findAllProductsByCriteria("", "true");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Product 1", result.get(0).getProductName());
    }

    @Test
    void testFindAllProductsByCriteria_NoCriteria() {
        when(productRepository.findAll()).thenReturn(products);

        List<ProductDTO> result = productService.findAllProductsByCriteria("", "");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Product 1", result.get(0).getProductName());
    }

    @Test
    void testFindAllProductsByCriteria_NoMatch() {
        when(productRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(ResourceNotFoundException.class, () -> productService.findAllProductsByCriteria("Nonexistent Product", ""));
    }

    @Test
    void testFindOneProductById_ExistingId() {
        Product product = new Product(1, "Product 1", "Overview 1", "Description 1", BigDecimal.valueOf(10.0), "poster1.jpg", "image1.jpg", 4, true, true, true);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        ProductDTO result = productService.findOneProductById(1);

        assertNotNull(result);
        assertEquals("Product 1", result.getProductName());
    }

    @Test
    void testFindOneProductById_NonExistingId() {
        when(productRepository.findById(100)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.findOneProductById(100));
    }
}
