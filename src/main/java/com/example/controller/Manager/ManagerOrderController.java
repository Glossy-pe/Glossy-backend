package com.example.controller.Manager;

import com.example.dto.admin.response.page.PageResponse;
import com.example.dto.manager.request.order.ManagerOrderRequest;
import com.example.dto.manager.response.order.ManagerOrderResponse;
import com.example.dto.manager.response.order.ManagerOrderResponseFull;
import com.example.dto.manager.response.order_item.ManagerOrderItemResponse;
import com.example.service.manager.order.ManagerOrderRestFullService;
import com.example.service.manager.order.ManagerOrderService;
import com.example.service.manager.order_item.ManagerOrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/manager/orders")
@RequiredArgsConstructor
public class ManagerOrderController {

    @Autowired
    private ManagerOrderService managerOrderService;

    @Autowired
    private ManagerOrderRestFullService managerOrderRestFullService;

    @Autowired
    private ManagerOrderItemService orderItemService;

//    @GetMapping
//    public Mono<PageResponse<ManagerOrderResponse>> getAll(
//            @RequestParam(required = false) String name,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//
//        return managerOrderService.getAllOrders(
//                name,
//                PageRequest.of(page, size)
//        );
//    }

    @GetMapping("/full")
    public Mono<PageResponse<ManagerOrderResponseFull>> getAllFull(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long variantId,
            @RequestParam(required = false) Long orderStatusId,    // 👈
            @RequestParam(required = false) Boolean isPaid,
            @RequestParam(required = false) Boolean isSeparated,
            @RequestParam(required = false) Boolean isPacked,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return managerOrderRestFullService.getAllOrdersFull(
                name,
                variantId,
                orderStatusId,   // 👈
                isPaid,
                isSeparated,
                isPacked,
                PageRequest.of(page, size)
        );
    }

    @GetMapping("/{id}")
    public Mono<ManagerOrderResponse> getById(@PathVariable Long id) {
        return managerOrderService.getOrderById(id);
    }

    @GetMapping("/{id}/full")
    public Mono<ManagerOrderResponseFull> getFullById(@PathVariable Long id) {
        return managerOrderRestFullService.getOrderByIdFull(id);
    }



    @GetMapping("/{id}/order-items")
    public Flux<ManagerOrderItemResponse> getVariantsByProductId(@PathVariable Long id) {
        return orderItemService.getVariantByOrderId(id);
    }

    @PostMapping
    public Mono<ManagerOrderResponse> create(@RequestBody ManagerOrderRequest request) {
        return managerOrderService.create(request);
    }

    @PutMapping("/{id}")
    public Mono<ManagerOrderResponse> update(
            @PathVariable Long id,
            @RequestBody ManagerOrderRequest request
    ) {
        return managerOrderService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return managerOrderService.delete(id);
    }

}
