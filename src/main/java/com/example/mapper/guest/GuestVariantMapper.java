package com.example.mapper.guest;

import com.example.dto.guest.response.variant.GuestVariantResponseFull;
import com.example.entity.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GuestVariantMapper {
    @Mapping(target = "images", ignore = true)
    GuestVariantResponseFull toResponseFull(ProductVariant variant);
}
