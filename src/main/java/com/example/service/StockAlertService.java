package com.example.service;

import com.example.dtos.response.StockAlertResponse;
import com.example.entity.ProductVariant;
import com.example.entity.StockAlert;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.ProductVariantRepository;
import com.example.repository.StockAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockAlertService {

    private final StockAlertRepository stockAlertRepository;
    private final ProductVariantRepository variantRepository;

    // Sincroniza variantes con stock < 2 que aún no tienen StockAlert creada
@Transactional
public void syncAlerts() {

    // ── 1. Eliminar alertas cuya variante ya tiene stock suficiente ──
    stockAlertRepository.findAll().forEach(alert -> {
        if (alert.getProductVariant().getStock() >= 2) {
            stockAlertRepository.delete(alert);
        }
    });

    // ── 2. Crear alertas para variantes en zona crítica sin alerta ──
    variantRepository.findByStockLessThanAndDeletedFalse(2).forEach(variant -> {
        boolean existe = stockAlertRepository
            .findByProductVariantId(variant.getId())
            .isPresent();

        if (!existe) {
            StockAlert alert = new StockAlert();
            alert.setProductVariant(variant);
            stockAlertRepository.save(alert);
        }
    });
}


public List<StockAlertResponse> getActiveAlerts() {
        return stockAlertRepository.findActiveAlerts()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional
    public void dismiss(Long alertId) {
        StockAlert alert = stockAlertRepository.findById(alertId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Alerta no encontrada con id: " + alertId));

        alert.setDismissed(true);
        alert.setDismissedAtStock(alert.getProductVariant().getStock());
        alert.setDismissedAt(LocalDateTime.now());
        stockAlertRepository.save(alert);
    }

    private StockAlertResponse toResponse(StockAlert alert) {
        ProductVariant pv = alert.getProductVariant();

        String firstImageUrl = (pv.getImages() != null && !pv.getImages().isEmpty())
            ? pv.getImages().get(0).getUrl()   // ajusta al campo real de tu ProductVariantImage
            : null;

        return new StockAlertResponse(
            alert.getId(),
            pv.getId(),
            pv.getToneName(),
            pv.getToneCode(),
            pv.getStock(),
            alert.isDismissed(),
            alert.getDismissedAt(),
            firstImageUrl
        );
    }
}