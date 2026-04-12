package com.example.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dtos.request.ProductImageRequest;
import com.example.dtos.response.ProductImageResponse;
import com.example.service.ProductImageService;

import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("products/{productId}/images")
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductImageService productImageService;

    @PostMapping("")
    public ResponseEntity<List<ProductImageResponse>> addImages(
            @PathVariable Long productId,
            @RequestBody List<ProductImageRequest> requests) {  // 👈 asegúrate que @RequestBody esté
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productImageService.addImages(productId, requests));
    }

    @GetMapping("")
    public List<ProductImageResponse> findAll(@PathVariable Long productId) {
        return productImageService.findByProduct(productId);
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long productId,
            @PathVariable Long imageId) {
        productImageService.deleteImage(productId, imageId);
        return ResponseEntity.noContent().build();
    }
}
