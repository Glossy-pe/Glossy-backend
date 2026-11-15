package com.example.dto.manager.response.payment;

import java.time.LocalDateTime;

public record ManagerPaymentProofResponse(Long id, String url, String note, LocalDateTime createdAt) {}