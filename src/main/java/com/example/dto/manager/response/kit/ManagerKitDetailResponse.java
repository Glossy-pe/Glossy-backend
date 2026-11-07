package com.example.dto.manager.response.kit;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ManagerKitDetailResponse {
    private Long id;
    private String name;
    private Boolean active;
    private LocalDateTime createdAt;
    private List<ManagerKitItemResponse> items;
}