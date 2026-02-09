package com.example.repository;

import com.example.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    Optional<ProductVariant> findByIdAndProductId(Long id, Long productId);
}
