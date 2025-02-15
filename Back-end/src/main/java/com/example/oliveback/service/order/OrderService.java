package com.example.oliveback.service.order;

import com.example.oliveback.domain.order.Order;
import com.example.oliveback.domain.user.Role;
import com.example.oliveback.domain.user.Users;
import com.example.oliveback.dto.order.OrderRequest;
import com.example.oliveback.dto.order.OrderResponse;
import com.example.oliveback.exception.CustomException;
import com.example.oliveback.repository.order.OrderRepository;
import com.example.oliveback.repository.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public OrderResponse createOrder(String username, OrderRequest request) {
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(CustomException.UserNotFoundException::new);

        if (request.getQuantity() < 1) {
            throw new CustomException.InvalidOrderException();
        }

        Order newOrder = Order.builder()
                .user(user)
                .productName(request.getProductName())
                .quantity(request.getQuantity())
                .totalPrice(request.getTotalPrice())
                .orderDate(LocalDateTime.now())
                .build();

        orderRepository.save(newOrder);
        return OrderResponse.fromEntity(newOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getUserOrders(String username) {
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(CustomException.UserNotFoundException::new);

        return orderRepository.findByUser(user)
                .stream()
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders(String username) {
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(CustomException.UserNotFoundException::new);

        if (user.getRole() != Role.ADMIN) {
            throw new CustomException.AccessDeniedException();
        }

        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
