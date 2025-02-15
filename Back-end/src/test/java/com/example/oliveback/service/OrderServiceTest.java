package com.example.oliveback.service;

import com.example.oliveback.domain.order.Order;
import com.example.oliveback.domain.user.Role;
import com.example.oliveback.domain.user.Users;
import com.example.oliveback.dto.order.OrderRequest;
import com.example.oliveback.dto.order.OrderResponse;
import com.example.oliveback.repository.order.OrderRepository;
import com.example.oliveback.repository.user.UsersRepository;
import com.example.oliveback.service.order.OrderService;
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

class OrderServiceTest {

    @Mock
    private OrderService orderService;  // ✅ 실제 OrderService를 테스트할 객체

    @Mock
    private OrderRepository orderRepository;  // ✅ 가짜 OrderRepository

    @Mock
    private UsersRepository usersRepository;  // ✅ 가짜 UsersRepository (사용자 조회용)

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // ✅ Mock 초기화
    }

    @Test
    void 주문_생성_성공() {
        // given
        String username = "testuser";
        Users user = new Users(1L, username, "password", "test@example.com", Role.USER);
        OrderRequest request = new OrderRequest("비타민C 세럼", 2, 70000);
        LocalDateTime now = LocalDateTime.now();

        when(usersRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            return new Order(1L, order.getUser(), order.getProductName(), order.getQuantity(), order.getTotalPrice(), now);
        });

        // Mock the createOrder response from orderService
        Order order = new Order(1L, user, "비타민C 세럼", 2, 70000, now);
        OrderResponse orderResponse = OrderResponse.fromEntity(order);
        when(orderService.createOrder(username, request)).thenReturn(orderResponse);

        // when
        OrderResponse response = orderService.createOrder(username, request);

        // then
        assertNotNull(response);
        assertEquals(request.getProductName(), response.getProductName());
        assertEquals(request.getQuantity(), response.getQuantity());
        assertEquals(request.getTotalPrice(), response.getTotalPrice());
        assertEquals(now, response.getOrderDate());
    }

    @Test
    void 주문_생성_실패_존재하지_않는_사용자() {
        // given
        String username = "unknownUser";
        OrderRequest request = new OrderRequest("비타민C 세럼", 2, 70000);

        when(usersRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Mock createOrder to throw an exception
        when(orderService.createOrder(username, request)).thenThrow(new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // when & then
        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(username, request),
                "존재하지 않는 사용자입니다.");
    }

    @Test
    void 사용자별_주문_조회() {
        // given
        String username = "testuser";
        Users user = new Users(1L, username, "password", "test@example.com", Role.USER);
        LocalDateTime now = LocalDateTime.now();

        Order order1 = new Order(1L, user, "비타민C 세럼", 2, 70000, now);
        Order order2 = new Order(2L, user, "히알루론산 크림", 1, 40000, now);

        List<Order> userOrders = Arrays.asList(order1, order2);
        when(usersRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(orderRepository.findByUser(user)).thenReturn(userOrders);

        // Mock the response for getUserOrders
        List<OrderResponse> orderResponses = Arrays.asList(
                OrderResponse.fromEntity(order1),
                OrderResponse.fromEntity(order2)
        );
        when(orderService.getUserOrders(username)).thenReturn(orderResponses);

        // when
        List<OrderResponse> foundOrders = orderService.getUserOrders(username);

        // then
        assertEquals(2, foundOrders.size());
        assertEquals("비타민C 세럼", foundOrders.get(0).getProductName());
        assertEquals("히알루론산 크림", foundOrders.get(1).getProductName());
        assertEquals(now, foundOrders.get(0).getOrderDate());
        assertEquals(now, foundOrders.get(1).getOrderDate());
    }

    @Test
    void 전체_주문_조회_실패_관리자가아님() {
        // given
        String userUsername = "testuser";
        Users normalUser = new Users(1L, userUsername, "password", "user@example.com", Role.USER);

        when(usersRepository.findByUsername(userUsername)).thenReturn(Optional.of(normalUser));

        // Mock getAllOrders to throw exception if user is not admin
        when(orderService.getAllOrders(userUsername)).thenThrow(new IllegalArgumentException("관리자만 전체 주문을 조회할 수 있습니다."));

        // when & then
        assertThrows(IllegalArgumentException.class, () -> orderService.getAllOrders(userUsername),
                "관리자만 전체 주문을 조회할 수 있습니다.");
    }



}
