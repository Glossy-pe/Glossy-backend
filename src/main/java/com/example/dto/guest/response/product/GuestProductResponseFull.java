package com.example.dto.guest.response.product;

import com.example.dto.guest.response.image.GuestProductImageResponse;
import com.example.dto.guest.response.variant.GuestVariantResponseFull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GuestProductResponseFull {
    private Long id;

    private String name;

    private String description;

    private String fullDescription;

    private String label;

    private Boolean active = true;

    private Long categoryId;

//    private String slug;
    private List<GuestProductImageResponse> images;

    private List<GuestVariantResponseFull> variants;

}
