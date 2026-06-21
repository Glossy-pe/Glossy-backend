package com.example.mapper.guest;

import com.example.dto.guest.response.image.GuestProductImageResponse;
import com.example.entity.ProductImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GuestProductImageMapper {

    GuestProductImageResponse toResponse(ProductImage entity);
}
