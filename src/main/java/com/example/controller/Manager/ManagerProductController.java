package com.example.controller.Manager;

import com.example.dto.manager.response.images.ManagerProductImageResponse;
import com.example.dto.manager.response.variant.ManagerVariantResponse;
import com.example.service.manager.images.ManagerProductImageService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import com.example.dto.admin.response.page.PageResponse;
import com.example.dto.manager.request.product.ManagerProductRequest;
import com.example.dto.manager.response.product.ManagerProductResponse;
import com.example.dto.manager.response.product.ManagerProductResponseFull;
import com.example.service.manager.product.ManagerProductRestFullService;
import com.example.service.manager.product.ManagerProductService;
import com.example.service.manager.variant.ManagerVariantService;

@RestController
@RequestMapping("/manager/products")
@RequiredArgsConstructor
public class ManagerProductController {
    
    @Autowired
    private ManagerProductService managerProductService;

    @Autowired
    private ManagerProductRestFullService managerProductRestFullService;

    @Autowired
    private ManagerVariantService managerVariantService;

    @Autowired
    private ManagerProductImageService managerProductImageService;

    @GetMapping
    public Mono<PageResponse<ManagerProductResponseFull>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return managerProductRestFullService.getAllProductsFull(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public Mono<ManagerProductResponse> getById(@PathVariable Long id) {
        return managerProductService.getProductById(id);
    }

    @GetMapping("/{id}/variants")
    public Flux<ManagerVariantResponse> getVariantsByProductId(@PathVariable Long id) {
        return managerVariantService.getVariantByProductId(id);
    }

    @GetMapping("/{id}/images")
    public Flux<ManagerProductImageResponse> getByProductId(@PathVariable Long id) {
        return managerProductImageService.getByProductId(id);
    }

    @PostMapping
    public Mono<ManagerProductResponse> create(@RequestBody ManagerProductRequest request) {
        return managerProductService.create(request);
    }

    @PutMapping("/{id}")
    public Mono<ManagerProductResponse> update(
            @PathVariable Long id,
            @RequestBody ManagerProductRequest request
    ) {
        return managerProductService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return managerProductService.delete(id);
    }

    @GetMapping("/search")
    public Flux<ManagerProductResponseFull> search(@RequestParam String q) {
        return managerProductRestFullService.searchProductsFull(q);
    }
}
