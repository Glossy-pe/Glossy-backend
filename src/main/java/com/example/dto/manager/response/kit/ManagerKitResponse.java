package com.example.dto.manager.response.kit;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ManagerKitResponse {
    private Long id;
    private String name;
    private Boolean active;
    private LocalDateTime createdAt;
}