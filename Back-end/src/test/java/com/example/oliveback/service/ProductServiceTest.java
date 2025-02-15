package com.example.oliveback.service;

import com.example.oliveback.domain.product.Product;
import com.example.oliveback.domain.user.Role;
import com.example.oliveback.domain.user.Users;
import com.example.oliveback.dto.product.ProductRequest;
import com.example.oliveback.dto.product.ProductResponse;
import com.example.oliveback.exception.CustomException;
import com.example.oliveback.repository.product.ProductRepository;
import com.example.oliveback.repository.user.UsersRepository;
import com.example.oliveback.service.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UsersRepository usersRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 상품등록_성공() {
        // given
        String adminUsername = "adminuser";
        Users adminUser = new Users(1L, adminUsername, "password", "admin@example.com", Role.ADMIN);
        ProductRequest request = new ProductRequest("비타민C 세럼", 35000, "skincare", "피부 톤 개선", "https://example.com/image.jpg");

        when(usersRepository.findByUsername(adminUsername)).thenReturn(Optional.of(adminUser));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product product = invocation.getArgument(0);
            return new Product(1L, product.getName(), product.getPrice(), product.getCategory(), product.getDescription(), product.getImageUrl());
        });

        // when
        ProductResponse response = productService.createProduct(adminUsername, request);

        // then
        assertNotNull(response);
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getPrice(), response.getPrice());
        assertEquals(request.getCategory(), response.getCategory());
    }

    @Test
    void 상품등록_실패_관리자가아님() {
        // given
        String userUsername = "normaluser";
        Users normalUser = new Users(1L, userUsername, "password", "user@example.com", Role.USER);
        ProductRequest request = new ProductRequest("비타민C 세럼", 35000, "skincare", "피부 톤 개선", "https://example.com/image.jpg");

        when(usersRepository.findByUsername(userUsername)).thenReturn(Optional.of(normalUser));

        // when & then
        assertThrows(CustomException.AccessDeniedException.class, () -> productService.createProduct(userUsername, request));
    }

    @Test
    void 상품조회_성공() {
        // given
        Product product = new Product(1L, "비타민C 세럼", 35000, "skincare", "피부 톤 개선", "https://example.com/image.jpg");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // when
        ProductResponse response = productService.getProductById(1L);

        // then
        assertNotNull(response);
        assertEquals(product.getName(), response.getName());
        assertEquals(product.getPrice(), response.getPrice());
    }

    @Test
    void 상품조회_실패_존재하지않는상품() {
        // given
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.ProductNotFoundException.class, () -> productService.getProductById(999L));
    }

    @Test
    void 카테고리별_상품_조회() {
        // given
        Product product1 = new Product(1L, "비타민C 세럼", 35000, "skincare", "피부 톤 개선", "https://example.com/image.jpg");
        Product product2 = new Product(2L, "히알루론산 크림", 40000, "skincare", "보습 크림", "https://example.com/image2.jpg");

        List<Product> skincareProducts = Arrays.asList(product1, product2);
        when(productRepository.findByCategory(anyString())).thenReturn(skincareProducts);

        // when
        List<ProductResponse> foundProducts = productService.getProductsByCategory("skincare");

        // then
        assertEquals(2, foundProducts.size());
        assertEquals("비타민C 세럼", foundProducts.get(0).getName());
        assertEquals("히알루론산 크림", foundProducts.get(1).getName());
    }
}

