package com.example.repository;

import com.example.entity.StockAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StockAlertRepository extends JpaRepository<StockAlert, Long> {

    Optional<StockAlert> findByProductVariantId(Long variantId);

    // Variantes con stock < 2 que NO están descartadas
    // O que están descartadas pero su stock bajó más que cuando se descartaron
    @Query("""
        SELECT sa FROM StockAlert sa
        JOIN FETCH sa.productVariant pv
        LEFT JOIN FETCH pv.images
        WHERE pv.deleted = false
          AND pv.stock < 2
          AND (
            sa.dismissed = false
            OR (sa.dismissed = true AND pv.stock < sa.dismissedAtStock)
          )
        ORDER BY pv.stock ASC
    """)
    List<StockAlert> findActiveAlerts();


    List<StockAlert> findAllByDismissedTrue();

}