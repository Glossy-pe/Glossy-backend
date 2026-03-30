package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.ProductVariantImage;

public interface VariantImageRepository extends JpaRepository<ProductVariantImage, Long>{
    Optional<ProductVariantImage> findByIdAndProductVariantId(Long id, Long productVariantId);
    List<ProductVariantImage> findByProductVariantId(Long variantId);
}
