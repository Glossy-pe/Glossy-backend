package com.example.dto.manager.response.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ManagerOrderCalendarResponse {
    private Long id;
    private String orderCode;
    private String customerName;
    private Long orderStatusId;
    private LocalDateTime scheduledSendAt;
}