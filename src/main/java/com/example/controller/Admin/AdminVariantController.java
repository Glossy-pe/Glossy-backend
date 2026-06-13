package com.example.controller.Admin;

import com.example.dto.admin.request.variant.VariantRequest;
import com.example.dto.admin.response.variant.VariantResponse;
import com.example.service.variant.VariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/admin/variants")
@RequiredArgsConstructor
public class AdminVariantController {
    @Autowired
    private VariantService variantService;

    @GetMapping
    public Flux<VariantResponse> getAll() {
        return variantService.getAllVariants();
    }

    @GetMapping("/{id}")
    public Mono<VariantResponse> getById(@PathVariable Long id) {
        return variantService.getVariantById(id);
    }

    @PostMapping
    public Mono<VariantResponse> create(@RequestBody VariantRequest request) {
        return variantService.create(request);
    }

    @PutMapping("/{id}")
    public Mono<VariantResponse> update(
            @PathVariable Long id,
            @RequestBody VariantRequest request
    ) {
        return variantService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return variantService.delete(id);
    }
}
