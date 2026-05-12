package com.example.dtos.response;

import java.time.LocalDateTime;

public record StockAlertResponse(
    Long alertId,
    Long variantId,
    String variantName,
    String toneCode,
    int stock,
    boolean dismissed,
    LocalDateTime dismissedAt,
    String firstImageUrl   // null si no tiene imágenes
) {}