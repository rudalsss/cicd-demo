package com.example.oliveback.controller;

import com.example.oliveback.controller.product.ProductController;
import com.example.oliveback.dto.product.ProductRequest;
import com.example.oliveback.dto.product.ProductResponse;
import com.example.oliveback.service.product.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 상품_목록_조회_API_테스트() throws Exception {
        // given
        List<ProductResponse> responses = Arrays.asList(
                new ProductResponse(1L, "비타민C 세럼", 35000, "skincare", "피부 톤 개선", "https://example.com/image.jpg"),
                new ProductResponse(2L, "히알루론산 크림", 40000, "skincare", "보습 크림", "https://example.com/image2.jpg")
        );

        when(productService.getProducts(anyInt(), anyInt(), anyString())).thenReturn(responses);

        // when & then
        mockMvc.perform(get("/api/products")
                        .param("page", "0")
                        .param("size", "10")
                        .param("category", "skincare"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("비타민C 세럼"))
                .andExpect(jsonPath("$[1].name").value("히알루론산 크림"));
    }

    @Test
    void 상품_상세_조회_API_테스트() throws Exception {
        // given
        ProductResponse response = new ProductResponse(1L, "비타민C 세럼", 35000, "skincare", "피부 톤 개선", "https://example.com/image.jpg");

        when(productService.getProductById(1L)).thenReturn(response);

        // when & then
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("비타민C 세럼"));
    }

    @Test
    void 상품_등록_API_테스트() throws Exception {
        // given
        ProductRequest request = new ProductRequest("비타민C 세럼", 35000, "skincare", "피부 톤 개선", "https://example.com/image.jpg");
        ProductResponse response = new ProductResponse(1L, "비타민C 세럼", 35000, "skincare", "피부 톤 개선", "https://example.com/image.jpg");

        when(productService.createProduct(anyString(), any(ProductRequest.class))).thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/products/adminuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("비타민C 세럼"));
    }
}
