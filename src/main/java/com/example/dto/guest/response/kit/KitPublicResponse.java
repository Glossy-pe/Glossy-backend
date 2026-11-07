package com.example.dto.guest.response.kit;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KitPublicResponse {
    private Long id;
    private String name;
    private List<KitItemPublicResponse> items;
}