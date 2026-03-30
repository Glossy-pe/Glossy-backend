package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.ProductLabel;

public interface ProductLabelRepository extends JpaRepository<ProductLabel, Long> {
    void deleteByProductId(Long productId);
}
