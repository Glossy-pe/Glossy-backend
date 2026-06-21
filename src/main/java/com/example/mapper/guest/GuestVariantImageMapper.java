package com.example.mapper.guest;

import com.example.dto.guest.response.image.GuestVariantImageResponse;
import com.example.entity.ProductVariantImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GuestVariantImageMapper {
    GuestVariantImageResponse toResponse(ProductVariantImage variantImage);
}
