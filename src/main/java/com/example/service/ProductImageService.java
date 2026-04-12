package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dtos.request.ProductImageRequest;
import com.example.dtos.response.ProductImageResponse;
import com.example.entity.Product;
import com.example.entity.ProductImage;
import com.example.mapper.ProductMapper;
import com.example.repository.ProductImageRepository;
import com.example.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductImageService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductMapper productMapper;

    @Transactional
    public List<ProductImageResponse> addImages(Long productId, List<ProductImageRequest> requests) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        List<ProductImage> images = requests.stream()
                .map(req -> {
                    ProductImage img = new ProductImage();
                    img.setUrl(req.getUrl());
                    img.setProduct(product);
                    return img;
                })
                .toList();

        return productImageRepository.saveAll(images).stream()
                .map(productMapper::toImageResponse)
                .toList();
    }

    @Transactional()
    public List<ProductImageResponse> findByProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new EntityNotFoundException("Product not found");
        }
        return productImageRepository.findByProductId(productId).stream()
                .map(productMapper::toImageResponse)
                .toList();
    }

    @Transactional
    public void deleteImage(Long productId, Long imageId) {
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("Image not found"));

        if (!image.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("Image does not belong to this product");
        }
        productImageRepository.deleteById(imageId);
    }
}
