package com.example.controller.Public;

import com.example.dto.admin.response.product.ProductResponseFull;
import com.example.service.product.ProductRestFullService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/public/products")
@RequiredArgsConstructor
public class PublicProductController {
    
    @Autowired
    private ProductRestFullService productRestFullService;


    @GetMapping("/full")
    public Flux<ProductResponseFull> getAllProductsFull() {
        return productRestFullService.getAllProductsFull();
    }


    @GetMapping("/full/{id}")
    public Mono<ProductResponseFull> getProductByIdFull(@PathVariable Long id) {
        return productRestFullService.getProductByIdFull(id);
    }
    
}
