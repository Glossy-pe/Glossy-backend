package com.example.controller;

import com.example.dtos.response.StockAlertResponse;
import com.example.service.StockAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock-alerts")
@RequiredArgsConstructor
public class StockAlertController {

    private final StockAlertService stockAlertService;

    // GET /stock-alerts → lista activa (tú llamas esto en tu refresh manual)
    @GetMapping
    public List<StockAlertResponse> getActiveAlerts() {
        stockAlertService.syncAlerts(); // sincroniza antes de devolver
        return stockAlertService.getActiveAlerts();
    }

    // PATCH /stock-alerts/{alertId}/dismiss → cierra la card
    @PatchMapping("/{alertId}/dismiss")
    public void dismiss(@PathVariable Long alertId) {
        stockAlertService.dismiss(alertId);
    }
}
