package com.example.service;

import com.example.dtos.response.StockAlertResponse;
import com.example.repository.ProductRepository;
import com.example.repository.projection.StockAlertProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockAlertService {

    private final ProductRepository productRepository;

@Transactional(readOnly = true)
public List<StockAlertResponse> getStockAlerts() {
    List<StockAlertProjection> rows = productRepository.findStockAlerts();

    Map<Long, List<StockAlertProjection>> byProduct = rows.stream()
            .collect(Collectors.groupingBy(
                    StockAlertProjection::getProductId,
                    LinkedHashMap::new,
                    Collectors.toList()
            ));

    return byProduct.entrySet().stream()
            .map(entry -> buildResponse(entry.getValue()))
            .sorted(Comparator.comparingDouble(StockAlertResponse::getUrgencyScore).reversed())
            .collect(Collectors.toList());
}

private StockAlertResponse buildResponse(List<StockAlertProjection> rows) {
    StockAlertProjection first = rows.get(0);

    // Todas las rows ya son variantes críticas (stock < 2 viene filtrado de la query)
    int totalStock = rows.stream()
            .mapToInt(r -> r.getVariantStock() != null ? r.getVariantStock() : 0)
            .sum();

    long recentSales = rows.stream()
            .mapToLong(r -> r.getVariantRecentSales() != null ? r.getVariantRecentSales() : 0)
            .sum();

    long hotSales = rows.stream()
            .mapToLong(r -> r.getVariantHotSales() != null ? r.getVariantHotSales() : 0)
            .sum();

    int outOfStockCount = (int) rows.stream()
            .filter(r -> r.getVariantStock() != null && r.getVariantStock() == 0)
            .count();

    int criticalCount = rows.size(); // todas son críticas

    List<StockAlertResponse.CriticalVariantDto> criticalVariants = rows.stream()
            .sorted(Comparator.comparingInt(r -> r.getVariantStock() != null ? r.getVariantStock() : 0))
            .map(r -> StockAlertResponse.CriticalVariantDto.builder()
                    .variantId(r.getVariantId())
                    .toneName(r.getToneName())
                    .toneCode(r.getToneCode())
                    .stock(r.getVariantStock() != null ? r.getVariantStock() : 0)
                    .recentSales(r.getVariantRecentSales() != null ? r.getVariantRecentSales() : 0)
                    .hotSales(r.getVariantHotSales() != null ? r.getVariantHotSales() : 0)
                    .build())
            .collect(Collectors.toList());

    double score = calcScore(outOfStockCount, criticalCount, totalStock, recentSales, hotSales);

    return StockAlertResponse.builder()
            .productId(first.getProductId())
            .productName(first.getProductName())
            .productSlug(first.getProductSlug())
            .mainImageUrl(first.getMainImageUrl())
            .totalStock(totalStock)
            .criticalVariantCount(criticalCount)
            .outOfStockCount(outOfStockCount)
            .recentSales(recentSales)
            .hotSales(hotSales)
            .urgencyScore(score)
            .hasCritical(true) // siempre true porque la query ya filtró
            .criticalVariants(criticalVariants)
            .build();
}
private double calcScore(int outOfStock, int criticalCount, int totalStock, long recentSales, long hotSales) {
    double score = 0;

    // Ventas últimas 24h — peso máximo, esto es lo que más importa
    score += hotSales * 50.0;

    // Ventas últimos 7 días — peso secundario
    score += recentSales * 10.0;

    // Penalización por stock bajo — desempata entre productos con ventas similares
    score += Math.max(0, 100 - totalStock);

    // Variantes agotadas — señal de urgencia adicional
    score += outOfStock * 20.0;

    // Variantes con solo 1 unidad
    score += (criticalCount - outOfStock) * 5.0;

    return score;
}
}