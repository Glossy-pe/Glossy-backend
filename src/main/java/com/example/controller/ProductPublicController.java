package com.example.controller;

import com.example.dtos.response.full.ProductResponseFull;
import com.example.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("public/products")
@RequiredArgsConstructor
public class ProductPublicController {

    private final ProductService productService;

    @GetMapping("")
    public Page<ProductResponseFull> findAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(required = false) Long categoryId,
        @RequestParam(required = false) Long labelId
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.findAllFull(pageable, categoryId, labelId);
    }

    @GetMapping("/search")
    public Page<ProductResponseFull> search(
        @RequestParam(defaultValue = "") String q,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(required = false) Long categoryId,
        @RequestParam(required = false) Long labelId
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.searchFull(q, pageable, categoryId, labelId);
    }

    // 👇 por slug — para la página de detalle del producto
    @GetMapping("/{slug}")
    public ProductResponseFull findBySlug(@PathVariable String slug) {
        return productService.findBySlug(slug);
    }
}