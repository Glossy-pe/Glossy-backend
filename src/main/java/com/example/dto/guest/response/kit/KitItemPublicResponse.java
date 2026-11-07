package com.example.dto.guest.response.kit;

import com.example.dto.admin.response.product.ProductResponseFull;
import com.example.dto.guest.response.product.GuestProductResponseFull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KitItemPublicResponse {
    private Integer position;
    private GuestProductResponseFull product;
}