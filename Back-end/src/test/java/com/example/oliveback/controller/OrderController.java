package com.example.oliveback.controller;

import com.example.oliveback.controller.order.OrderController;
import com.example.oliveback.dto.order.OrderRequest;
import com.example.oliveback.dto.order.OrderResponse;
import com.example.oliveback.service.order.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 주문_생성_API_테스트() throws Exception {
        // given
        OrderRequest request = new OrderRequest("비타민C 세럼", 2, 70000);
        OrderResponse response = new OrderResponse(1L, "testuser", "비타민C 세럼", 2, 70000, LocalDateTime.now());

        when(orderService.createOrder(anyString(), any(OrderRequest.class))).thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/orders/testuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("비타민C 세럼"));
    }

    @Test
    void 사용자별_주문_조회_API_테스트() throws Exception {
        // given
        List<OrderResponse> responses = Arrays.asList(
                new OrderResponse(1L, "testuser", "비타민C 세럼", 2, 70000, LocalDateTime.now()),
                new OrderResponse(2L, "testuser", "히알루론산 크림", 1, 40000, LocalDateTime.now())
        );

        when(orderService.getUserOrders(anyString())).thenReturn(responses);

        // when & then
        mockMvc.perform(get("/api/orders/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].productName").value("비타민C 세럼"))
                .andExpect(jsonPath("$[1].productName").value("히알루론산 크림"));
    }

    @Test
    void 전체_주문_조회_API_테스트() throws Exception {
        // given
        List<OrderResponse> responses = Arrays.asList(
                new OrderResponse(1L, "testuser", "비타민C 세럼", 2, 70000, LocalDateTime.now()),
                new OrderResponse(2L, "adminuser", "히알루론산 크림", 1, 40000, LocalDateTime.now())
        );

        when(orderService.getAllOrders(anyString())).thenReturn(responses);

        // when & then
        mockMvc.perform(get("/api/orders/adminuser/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].productName").value("비타민C 세럼"))
                .andExpect(jsonPath("$[1].productName").value("히알루론산 크림"));
    }
}
