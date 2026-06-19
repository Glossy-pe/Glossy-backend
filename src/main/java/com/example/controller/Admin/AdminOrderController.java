package com.example.controller.Admin;

import com.example.dto.admin.request.order.OrderRequest;
import com.example.dto.admin.response.order.OrderResponse;
import com.example.dto.admin.response.order_item.OrderItemResponse;
import com.example.dto.admin.response.page.PageResponse;
import com.example.service.order.OrderService;
import com.example.service.order_item.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

//    @GetMapping
//    public Mono<PageResponse<OrderResponse>> getAll(
//            @RequestParam(required = false) String name,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//
//        return orderService.getAllOrders(
//                name,
//                PageRequest.of(page, size)
//        );
//    }

    @GetMapping("/{id}")
    public Mono<OrderResponse> getById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @GetMapping("/{id}/order-items")
    public Flux<OrderItemResponse> getVariantsByProductId(@PathVariable Long id) {
        return orderItemService.getVariantByOrderId(id);
    }

    @PostMapping
    public Mono<OrderResponse> create(@RequestBody OrderRequest request) {
        return orderService.create(request);
    }

    @PutMapping("/{id}")
    public Mono<OrderResponse> update(
            @PathVariable Long id,
            @RequestBody OrderRequest request
    ) {
        return orderService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return orderService.delete(id);
    }

}
