package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.StockAlert;

public interface StockAlertRepository extends JpaRepository<StockAlert, Long> {
    List<StockAlert> findByDismissedFalseOrderByOccurredAtDesc();

    Optional<StockAlert> findByVariantIdAndDismissedFalse(Long variantId);

}