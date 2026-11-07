package com.example.dto.manager.request.kit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerKitRequest {
    private String name;
    private Boolean active = true;
}