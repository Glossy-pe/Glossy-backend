package com.example.dto.manager.response.kit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerKitItemResponse {
    private Long id;
    private Long kitId;
    private Long productId;
    private String productName;   // se llena en el servicio
    private Integer position;
}