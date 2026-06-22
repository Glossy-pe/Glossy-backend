package com.example.dto.guest.response.order_status;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GuestOrderStatusResponse {
    private Long id;

    private String code;

    private String description;

    private String hexColor;
}
