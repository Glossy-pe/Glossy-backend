package com.example.controller.Manager;

import com.example.dto.manager.request.variant.ManagerVariantRequest;
import com.example.dto.manager.response.variant.ManagerVariantResponse;
import com.example.dto.manager.response.variant.ManagerVariantQueryProjectionResponse;
import com.example.service.manager.variant.ManagerVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/manager/variants")
@RequiredArgsConstructor
public class ManagerVariantController {
    @Autowired
    private ManagerVariantService managerVariantService;

    @GetMapping
    public Flux<ManagerVariantResponse> getAll() {
        return managerVariantService.getAllVariants();
    }

    @GetMapping("/{id}")
    public Mono<ManagerVariantResponse> getById(@PathVariable Long id) {
        return managerVariantService.getVariantById(id);
    }

    @GetMapping("/query-projection/{variantId}")
    public Mono<ManagerVariantQueryProjectionResponse> getVariantQueryProjectionDetail(@PathVariable Long variantId) {
        return managerVariantService.getVariantDetail(variantId);
    }

    @PostMapping
    public Mono<ManagerVariantResponse> create(@RequestBody ManagerVariantRequest request) {
        return managerVariantService.create(request);
    }

    @PutMapping("/{id}")
    public Mono<ManagerVariantResponse> update(
            @PathVariable Long id,
            @RequestBody ManagerVariantRequest request
    ) {
        return managerVariantService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return managerVariantService.delete(id);
    }
}
