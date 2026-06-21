package com.example.mapper.guest;

import com.example.dto.guest.response.product.GuestProductResponseFull;
import com.example.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GuestProductMapper {

    @Mapping(target = "variants", ignore = true)
    @Mapping(target = "images", ignore = true)
    GuestProductResponseFull toResponseFull(Product product);
}
