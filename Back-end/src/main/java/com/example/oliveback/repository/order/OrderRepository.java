package com.example.oliveback.repository.order;

import com.example.oliveback.domain.order.Order;
import com.example.oliveback.domain.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(Users user);
}
