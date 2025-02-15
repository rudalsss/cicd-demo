package com.example.oliveback.controller.order;

import com.example.oliveback.dto.order.OrderRequest;
import com.example.oliveback.dto.order.OrderResponse;
import com.example.oliveback.service.order.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{username}")
    public ResponseEntity<OrderResponse> createOrder(
            @PathVariable String username,
            @Valid @RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(username, request));
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<OrderResponse>> getUserOrders(@PathVariable String username) {
        return ResponseEntity.ok(orderService.getUserOrders(username));
    }

    @GetMapping("/{username}/all")
    public ResponseEntity<List<OrderResponse>> getAllOrders(@PathVariable String username) {
        return ResponseEntity.ok(orderService.getAllOrders(username));
    }
}

