package com.example.dto.manager.response.product;

import java.time.LocalDateTime;
import java.util.List;


// Respuesta final
public record ManagerProductOrderResponse(
        Long orderId,
        String orderCode,
        String customerName,
        String customerAddress,
        Long orderStatusId,
        LocalDateTime createdAt,
        List<ManagerProductOrderVariantResponse> variants
) {}
