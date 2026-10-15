package com.example.dto.manager.response.product;

import java.math.BigDecimal;

// Fila de la query 2: una por order item de ese producto
public record ManagerProductOrderVariantResponse(
        Long orderItemId,
        Long orderId,
        Long variantId,
        String toneName,
        String toneCode,
        BigDecimal unitPrice,
        BigDecimal amountPaid,
        Boolean isSeparated,
        Boolean isPacked
) {}