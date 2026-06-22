package com.example.dto.guest.response.auditable;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public abstract class GuestAuditableResponse {
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
