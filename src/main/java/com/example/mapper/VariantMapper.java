package com.example.mapper;

import com.example.dto.admin.request.variant.VariantRequest;
import com.example.dto.admin.response.variant.VariantResponse;
import com.example.dto.admin.response.variant.VariantResponseFull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.entity.ProductVariant;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface VariantMapper {
    
    @Mapping(target = "images", ignore = true)
    VariantResponseFull toResponseFull(ProductVariant variant);

    VariantResponse toResponse(ProductVariant variant);

    ProductVariant toEntity(VariantRequest request);

    void updateEntity(VariantRequest request, @MappingTarget ProductVariant variant);

}
