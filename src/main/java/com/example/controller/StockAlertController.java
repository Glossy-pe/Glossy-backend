package com.example.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.StockAlert;
import com.example.repository.StockAlertRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stock-alerts")
@RequiredArgsConstructor
public class StockAlertController {

    private final StockAlertRepository stockAlertRepository;

    @GetMapping
    public List<StockAlert> getActive() {
        return stockAlertRepository.findByDismissedFalseOrderByOccurredAtDesc();
    }

    @PatchMapping("/{id}/dismiss")
    public void dismiss(@PathVariable Long id) {
        stockAlertRepository.findById(id).ifPresent(alert -> {
            alert.setDismissed(true);
            stockAlertRepository.save(alert);
        });
    }
}
