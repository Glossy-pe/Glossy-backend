package com.example.dto.manager.response.auditable;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class AuditableResponse {

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
