package com.example.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.dtos.request.VariantRequest;
import com.example.dtos.response.VariantResponse;
import com.example.entity.ProductVariant;
import com.example.mapper.VariantMapper;
import com.example.service.VariantService;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/variants")
@RequiredArgsConstructor
public class VariantController {

    private final VariantService variantService;
    private final VariantMapper variantMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VariantResponse create(@Valid @RequestBody VariantRequest variantRequest) {
        ProductVariant productVariant = variantMapper.toEntity(variantRequest);
        return variantMapper.toResponse(variantService.create(productVariant));
    }

    @GetMapping
    public List<VariantResponse> findByProductId(@RequestParam Long productId) {
        return variantService.findByProductId(productId).stream()
                .map(variantMapper::toResponse)
                .toList();
    }

    @GetMapping("/{variantId}")
    public VariantResponse findById(@PathVariable Long variantId) {
        return variantMapper.toResponse(variantService.findById(variantId));
    }

    @PutMapping("/{variantId}")
    public VariantResponse update(@PathVariable Long variantId,
                                  @Valid @RequestBody VariantRequest variantRequest) {
        ProductVariant productVariant = variantMapper.toEntity(variantRequest);
        return variantMapper.toResponse(variantService.update(variantId, productVariant));
    }

    @DeleteMapping("/{variantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long variantId) {
        variantService.delete(variantId);
    }
}