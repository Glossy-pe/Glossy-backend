package com.example.controller.guest;

import com.example.dto.guest.response.order.GuestOrderResponseFull;
import com.example.service.guest.order.GuestOrderRestFullService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/guest/orders")
@RequiredArgsConstructor
public class PublicOrderController {

    @Autowired
    private GuestOrderRestFullService orderRestFullService;

    @GetMapping("/full/{id}")
    public Mono<GuestOrderResponseFull> getOrderByIdFull(@PathVariable Long id) {
        return orderRestFullService.getOrderByIdFull(id);
    }

    @GetMapping("/by-token/{token}")
    public Mono<GuestOrderResponseFull> getOrderByToken(@PathVariable String token) {
        return orderRestFullService.getOrderByToken(token);
    }
}
