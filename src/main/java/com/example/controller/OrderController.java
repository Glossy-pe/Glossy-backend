package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;

import com.example.dtos.request.OrderRequest;
import com.example.dtos.response.OrderResponse;
import com.example.mapper.OrderMapper;
import com.example.service.OrderService;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @GetMapping
    public Page<OrderResponse> readAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "") String status
    ) {
        List<String> statusList = status.isBlank()
                ? List.of()
                : List.of(status.split(","));

        return orderService.findAll(q, statusList, PageRequest.of(page, size))
                .map(orderMapper::toResponse);
    }

    @GetMapping("/{orderId}")
    public OrderResponse read(@PathVariable Long orderId) {
        return orderMapper.toResponse(orderService.findById(orderId));
    }

    @PostMapping("")
    public OrderResponse create(@RequestBody OrderRequest orderRequest) {
        return orderMapper.toResponse(orderService.create(orderRequest));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> delete(@PathVariable Long orderId) {
        orderService.delete(orderId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{orderId}")
    public OrderResponse update(@PathVariable Long orderId, @RequestBody OrderRequest orderRequest) {
        return orderMapper.toResponse(orderService.update(orderId, orderRequest));
    }
}
