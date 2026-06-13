package com.example.mapper.manager;

import com.example.dto.manager.request.variant.ManagerVariantRequest;
import com.example.dto.manager.response.variant.ManagerVariantResponse;
import com.example.dto.manager.response.variant.ManagerVariantResponseFull;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.entity.ProductVariant;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface ManagerVariantMapper {
    
    @Mapping(target = "images", ignore = true)
    ManagerVariantResponseFull toResponseFull(ProductVariant variant);

    ManagerVariantResponse toResponse(ProductVariant variant);

    ProductVariant toEntity(ManagerVariantRequest request);

    void updateEntity(ManagerVariantRequest request, @MappingTarget ProductVariant variant);

}
