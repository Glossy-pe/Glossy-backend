package com.example.controller.Manager;

import com.example.dto.admin.request.order_item.OrderItemRequest;
import com.example.dto.admin.response.order_item.OrderItemResponse;
import com.example.dto.manager.request.order_item.ManagerOrderItemRequest;
import com.example.dto.manager.response.order_item.ManagerOrderItemResponse;
import com.example.service.manager.order_item.ManagerOrderItemService;
import com.example.service.order_item.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/manager/order-items")
@RequiredArgsConstructor
public class ManagerOrderItemController {
    @Autowired
    private ManagerOrderItemService managerOrderItemService;

    @GetMapping
    public Flux<ManagerOrderItemResponse> getAll() {
        return managerOrderItemService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<ManagerOrderItemResponse> getById(@PathVariable Long id) {
        return managerOrderItemService.getOrderItemById(id);
    }

    @PostMapping
    public Mono<ManagerOrderItemResponse> create(@RequestBody ManagerOrderItemRequest request) {
        return managerOrderItemService.create(request);
    }

    @PutMapping("/{id}")
    public Mono<ManagerOrderItemResponse> update(
            @PathVariable Long id,
            @RequestBody ManagerOrderItemRequest request
    ) {
        return managerOrderItemService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return managerOrderItemService.delete(id);
    }
}
