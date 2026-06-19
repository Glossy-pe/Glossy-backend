package com.example.controller.Public;

import com.example.dto.admin.response.order.OrderResponseFull;
import com.example.dto.admin.response.page.PageResponse;
import com.example.service.order.OrderRestFullService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/public/orders")
@RequiredArgsConstructor
public class PublicOrderController {

    @Autowired
    private OrderRestFullService orderRestFullService;


//    @GetMapping("/full")
//    public Mono<PageResponse<OrderResponseFull>> getAllOrdersFull(
//            @RequestParam(required = false) String name,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//
//        return orderRestFullService.getAllOrdersFull(
//                name,
//                PageRequest.of(page, size)
//        );
//    }


    @GetMapping("/full/{id}")
    public Mono<OrderResponseFull> getOrderByIdFull(@PathVariable Long id) {
        return orderRestFullService.getOrderByIdFull(id);
    }
}
