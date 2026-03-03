package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dtos.request.OrderRequest;
import com.example.dtos.response.OrderResponse;
import com.example.entity.Order;
import com.example.mapper.OrderMapper;
import com.example.service.OrderService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @GetMapping("")
    public List<OrderResponse> readAll() {
        return orderService.findAll().stream().map(orderMapper::toResponse).toList();
    }

    @GetMapping("/{orderId}")
    public OrderResponse read(@PathVariable("orderId") Long orderId) {
        return orderMapper.toResponse(orderService.findById(orderId));
    }
    
    @PostMapping("")
    public OrderResponse create(@RequestBody OrderRequest orderRequest) {
        return orderMapper.toResponse(orderService.create(orderRequest));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> delete(@PathVariable("orderId") Long orderId) {
        orderService.delete(orderId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{orderId}")
    public OrderResponse update(@PathVariable("orderId") Long orderId, @RequestBody OrderRequest orderRequest) {
        return orderMapper.toResponse(orderService.update(orderId, orderRequest));
    }
    
}
