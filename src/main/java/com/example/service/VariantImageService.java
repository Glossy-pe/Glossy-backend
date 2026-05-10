package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.ProductVariant;
import com.example.entity.ProductVariantImage;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.ProductVariantRepository;
import com.example.repository.VariantImageRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VariantImageService {

    private final VariantImageRepository imageRepository;
    private final ProductVariantRepository variantRepository;

    @Transactional
    public ProductVariantImage create(ProductVariantImage image) {
        ProductVariant variant = variantRepository.findById(image.getProductVariant().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Variante no encontrada con id: " + image.getProductVariant().getId()));

        image.setProductVariant(variant);
        return imageRepository.save(image);
    }

    public ProductVariantImage findById(Long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Imagen no encontrada con id: " + imageId));
    }

    public List<ProductVariantImage> findByVariantId(Long variantId) {
        return imageRepository.findByProductVariantId(variantId);
    }

    @Transactional
    public ProductVariantImage update(Long imageId, ProductVariantImage updatedImage) {
        ProductVariantImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Imagen no encontrada con id: " + imageId));

        image.setUrl(updatedImage.getUrl());
        image.setPosition(updatedImage.getPosition());
        image.setMainImage(updatedImage.isMainImage());

        return imageRepository.save(image);
    }

    @Transactional
    public void delete(Long imageId) {
        ProductVariantImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Imagen no encontrada con id: " + imageId));

        imageRepository.delete(image);
    }
}