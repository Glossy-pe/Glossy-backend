package com.example.dto.manager.request.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ManagerOrderScheduleRequest {
    private LocalDateTime scheduledSendAt; // null = quitar la programación
}