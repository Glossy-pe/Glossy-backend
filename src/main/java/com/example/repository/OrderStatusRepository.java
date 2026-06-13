package com.example.repository;

import com.example.entity.Order;
import com.example.entity.OrderStatus;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface OrderStatusRepository extends ReactiveCrudRepository<OrderStatus, Long> {
}
