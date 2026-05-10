package com.example.repository;

import com.example.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    // ── Con soft delete ──────────────────────────────────────────
    Optional<ProductVariant> findByIdAndDeletedFalse(Long id);
    List<ProductVariant> findByProductIdAndDeletedFalse(Long productId);

    // ── Existentes ───────────────────────────────────────────────
    Optional<ProductVariant> findByIdAndProductId(Long id, Long productId);
    List<ProductVariant> findByProductId(Long productId);
}