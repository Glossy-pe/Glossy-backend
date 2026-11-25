package com.example.controller.Manager;

import com.example.dto.manager.request.pucharse.ManagerBoxPurchaseRequest;
import com.example.dto.manager.request.pucharse.ManagerPurchaseRequest;
import com.example.dto.manager.response.pucharse.ManagerPurchaseResponse;
import com.example.service.manager.pucharse.ManagerBoxPurchaseService;
import com.example.service.manager.pucharse.ManagerPurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/manager/purchases")
@RequiredArgsConstructor
public class ManagerPurchaseController {

    @Autowired
    private ManagerPurchaseService purchaseService;
    @Autowired
    private ManagerBoxPurchaseService boxPurchaseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ManagerPurchaseResponse> create(@RequestBody ManagerPurchaseRequest request) {
        return purchaseService.create(request);
    }

    @GetMapping("/{id}")
    public Mono<ManagerPurchaseResponse> getById(@PathVariable Long id) {
        return purchaseService.getById(id);
    }

    @GetMapping
    public Flux<ManagerPurchaseResponse> getByProduct(@RequestParam Long productId) {
        return purchaseService.getByProductId(productId);
    }

    @PostMapping("/box")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<List<ManagerPurchaseResponse>> registerBoxes(@RequestBody ManagerBoxPurchaseRequest request) {
        return boxPurchaseService.registerBoxes(request);
    }
}