package com.example.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.dtos.request.VariantRequest;
import com.example.dtos.response.VariantResponse;
import com.example.entity.ProductVariant;

@Mapper(componentModel = "spring")
public interface VariantMapper {
    
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "images", source = "images")
    VariantResponse toResponse(ProductVariant productVariant);

    @Mapping(target = "product.id", source = "productId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "images", ignore = true)
    ProductVariant toEntity(VariantRequest variantRequest);
}
