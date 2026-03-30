// ProductVariantImageController.java
package com.example.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.dtos.request.VariantImageRequest;
import com.example.dtos.response.VariantImageResponse;
import com.example.entity.ProductVariantImage;
import com.example.mapper.VariantImageMapper;
import com.example.service.VariantImageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/variant-images")
@RequiredArgsConstructor
public class VariantImageController {

    private final VariantImageService imageService;
    private final VariantImageMapper imageMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VariantImageResponse create(@Valid @RequestBody VariantImageRequest request) {
        ProductVariantImage image = imageMapper.toEntity(request);
        return imageMapper.toResponse(imageService.create(image));
    }

    @GetMapping
    public List<VariantImageResponse> findByVariantId(@RequestParam Long variantId) {
        return imageService.findByVariantId(variantId).stream()
                .map(imageMapper::toResponse)
                .toList();
    }

    @GetMapping("/{imageId}")
    public VariantImageResponse findById(@PathVariable Long imageId) {
        return imageMapper.toResponse(imageService.findById(imageId));
    }

    @PutMapping("/{imageId}")
    public VariantImageResponse update(@PathVariable Long imageId,
                                              @Valid @RequestBody VariantImageRequest request) {
        ProductVariantImage image = imageMapper.toEntity(request);
        return imageMapper.toResponse(imageService.update(imageId, image));
    }

    @DeleteMapping("/{imageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long imageId) {
        imageService.delete(imageId);
    }
}