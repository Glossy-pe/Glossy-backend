package com.example.repository.projection;

public interface StockAlertProjection {
    Long getProductId();
    String getProductName();
    String getProductSlug();
    Long getVariantId();
    String getToneName();
    String getToneCode();
    Integer getVariantStock();
    Long getVariantRecentSales();  // ventas 7 días
    Long getVariantHotSales();     // ventas 1 día  👈
    String getMainImageUrl();
}