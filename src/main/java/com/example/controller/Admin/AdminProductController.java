package com.example.controller.Admin;

import com.example.service.variant.VariantService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import com.example.dto.admin.request.product.ProductRequest;
import com.example.dto.admin.response.page.PageResponse;
import com.example.dto.admin.response.product.ProductResponse;
import com.example.dto.admin.response.variant.VariantResponse;
import com.example.service.product.ProductService;

@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {
    
    @Autowired
    private ProductService productService;

    @Autowired
    private VariantService variantService;

    @GetMapping
    public Mono<PageResponse<ProductResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return productService.getAllProducts(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public Mono<ProductResponse> getById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/{id}/variants")
    public Flux<VariantResponse> getVariantsByProductId(@PathVariable Long id) {
        return variantService.getVariantByProductId(id);
    }

    @PostMapping
    public Mono<ProductResponse> create(@RequestBody ProductRequest request) {
        return productService.create(request);
    }

    @PutMapping("/{id}")
    public Mono<ProductResponse> update(
            @PathVariable Long id,
            @RequestBody ProductRequest request
    ) {
        return productService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return productService.delete(id);
    }
}
