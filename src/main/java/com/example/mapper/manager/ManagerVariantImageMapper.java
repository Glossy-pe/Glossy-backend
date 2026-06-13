package com.example.mapper.manager;

import org.mapstruct.Mapper;

import com.example.dto.manager.response.ManagerVariantImageResponse;
import com.example.entity.ProductVariantImage;

@Mapper(componentModel = "spring")
public interface ManagerVariantImageMapper {
    
    ManagerVariantImageResponse toResponse(ProductVariantImage variantImage);
}
