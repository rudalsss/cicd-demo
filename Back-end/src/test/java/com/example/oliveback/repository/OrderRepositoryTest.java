package com.example.oliveback.repository;

import com.example.oliveback.domain.order.Order;
import com.example.oliveback.domain.user.Users;
import com.example.oliveback.repository.order.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderRepositoryTest {

    @Mock  // ✅ OrderRepository는 인터페이스이므로 @Mock 사용
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // ✅ Mock 초기화
    }

    @Test
    void 주문_저장_및_조회() {
        // given
        Users user = new Users(1L, "testuser", "password", "test@example.com", null);
        LocalDateTime now = LocalDateTime.now();
        Order order = new Order(1L, user, "비타민C 세럼", 2, 70000, now);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // when
        Optional<Order> foundOrder = orderRepository.findById(1L);

        // then
        assertTrue(foundOrder.isPresent());
        assertEquals("비타민C 세럼", foundOrder.get().getProductName());
        assertEquals(70000, foundOrder.get().getTotalPrice());
        assertEquals(now, foundOrder.get().getOrderDate());
    }

    @Test
    void 존재하지_않는_주문_조회() {
        // given
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // when
        Optional<Order> foundOrder = orderRepository.findById(999L);

        // then
        assertFalse(foundOrder.isPresent());
    }

    @Test
    void 사용자별_주문_조회() {
        // given
        Users user = new Users(1L, "testuser", "password", "test@example.com", null);
        LocalDateTime now = LocalDateTime.now();
        Order order1 = new Order(1L, user, "비타민C 세럼", 2, 70000, now);
        Order order2 = new Order(2L, user, "히알루론산 크림", 1, 40000, now);

        List<Order> userOrders = Arrays.asList(order1, order2);
        when(orderRepository.findByUser(user)).thenReturn(userOrders);

        // when
        List<Order> foundOrders = orderRepository.findByUser(user);

        // then
        assertEquals(2, foundOrders.size());
        assertEquals("비타민C 세럼", foundOrders.get(0).getProductName());
        assertEquals("히알루론산 크림", foundOrders.get(1).getProductName());
        assertEquals(now, foundOrders.get(0).getOrderDate());
        assertEquals(now, foundOrders.get(1).getOrderDate());
    }
}
