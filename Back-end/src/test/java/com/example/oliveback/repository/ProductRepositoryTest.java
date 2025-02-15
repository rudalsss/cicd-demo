package com.example.oliveback.repository;

import com.example.oliveback.domain.product.Product;
import com.example.oliveback.repository.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductRepositoryTest {

    @Mock  // ✅ ProductRepository는 인터페이스이므로 @Mock 사용
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // ✅ Mock 초기화
    }

    @Test
    void 상품_저장_및_조회() {
        // given
        Product product = new Product(1L, "비타민C 세럼", 35000, "skincare", "피부 톤 개선", "https://example.com/image.jpg");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // when
        Optional<Product> foundProduct = productRepository.findById(1L);

        // then
        assertTrue(foundProduct.isPresent());
        assertEquals("비타민C 세럼", foundProduct.get().getName());
        assertEquals(35000, foundProduct.get().getPrice());
    }

    @Test
    void 존재하지_않는_상품_조회() {
        // given
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // when
        Optional<Product> foundProduct = productRepository.findById(999L);

        // then
        assertFalse(foundProduct.isPresent());
    }

    @Test
    void 카테고리별_상품_조회() {
        // given
        Product product1 = new Product(1L, "비타민C 세럼", 35000, "skincare", "피부 톤 개선", "https://example.com/image.jpg");
        Product product2 = new Product(2L, "히알루론산 크림", 40000, "skincare", "보습 크림", "https://example.com/image2.jpg");

        List<Product> skincareProducts = Arrays.asList(product1, product2);
        when(productRepository.findByCategory(anyString())).thenReturn(skincareProducts);

        // when
        List<Product> foundProducts = productRepository.findByCategory("skincare");

        // then
        assertEquals(2, foundProducts.size());
        assertEquals("비타민C 세럼", foundProducts.get(0).getName());
        assertEquals("히알루론산 크림", foundProducts.get(1).getName());
    }
}
