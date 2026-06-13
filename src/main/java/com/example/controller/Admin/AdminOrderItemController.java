package com.example.controller.Admin;

import com.example.dto.admin.request.order_item.OrderItemRequest;
import com.example.dto.admin.response.order_item.OrderItemResponse;
import com.example.service.order_item.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/admin/order-items")
@RequiredArgsConstructor
public class AdminOrderItemController {
    @Autowired
    private OrderItemService orderItemService;

    @GetMapping
    public Flux<OrderItemResponse> getAll() {
        return orderItemService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<OrderItemResponse> getById(@PathVariable Long id) {
        return orderItemService.getOrderItemById(id);
    }

    @PostMapping
    public Mono<OrderItemResponse> create(@RequestBody OrderItemRequest request) {
        return orderItemService.create(request);
    }

    @PutMapping("/{id}")
    public Mono<OrderItemResponse> update(
            @PathVariable Long id,
            @RequestBody OrderItemRequest request
    ) {
        return orderItemService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return orderItemService.delete(id);
    }
}
