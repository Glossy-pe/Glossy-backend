package com.example.dto.manager.response.product;

import java.time.LocalDateTime;

// Fila de la query 1 (sin lista)
public record ManagerProductOrderRow(
        Long orderId,
        String orderCode,
        String customerName,
        String customerAddress,
        Long orderStatusId,
        LocalDateTime createdAt
) {}
