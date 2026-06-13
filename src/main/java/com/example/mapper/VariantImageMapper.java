package com.example.mapper;

import org.mapstruct.Mapper;

import com.example.dto.admin.response.VariantImageResponse;
import com.example.entity.ProductVariantImage;

@Mapper(componentModel = "spring")
public interface VariantImageMapper {
    
    VariantImageResponse toResponse(ProductVariantImage variantImage);
}
