package com.example.controller.guest;

import com.example.dto.admin.response.page.PageResponse;
import com.example.dto.guest.response.product.GuestProductResponseFull;
import com.example.service.guest.product.GuestProductRestFullService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/guest/products")
@RequiredArgsConstructor
public class GuestProductController {
    
    @Autowired
    private GuestProductRestFullService guestProductRestFullService;


    @GetMapping("/full")
    public Mono<PageResponse<GuestProductResponseFull>> getAllProductsFull(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String sort) {
        return guestProductRestFullService.getAllProductsFull(
                PageRequest.of(page, size), categoryId, sort);
    }

    @GetMapping("/{id}")
    public Mono<GuestProductResponseFull> getById(@PathVariable Long id) {
        return guestProductRestFullService.getProductByIdFull(id);
    }

    @GetMapping("/search")
    public Flux<GuestProductResponseFull> search(
            @RequestParam String q,
            @RequestParam(required = false) Long categoryId) {
        return guestProductRestFullService.searchProductsFull(q, categoryId);
    }

}
