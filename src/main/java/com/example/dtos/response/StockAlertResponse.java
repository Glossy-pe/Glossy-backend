package com.example.dtos.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StockAlertResponse {
    private Long productId;
    private String productName;
    private String productSlug;
    private String mainImageUrl;
    private int totalStock;
    private int criticalVariantCount;
    private int outOfStockCount;
    private long recentSales;    // 7 días
    private long hotSales;       // 1 día  👈
    private double urgencyScore;
    private boolean hasCritical;
    private List<CriticalVariantDto> criticalVariants;

    @Data
    @Builder
    public static class CriticalVariantDto {
        private Long variantId;
        private String toneName;
        private String toneCode;
        private int stock;
        private long recentSales;
        private long hotSales;   // 👈
    }
}