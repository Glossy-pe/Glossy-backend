package com.example.mapper.manager;

import com.example.dto.manager.request.images.ManagerVariantImageRequest;
import com.example.dto.manager.response.images.ManagerVariantImageResponse;
import org.mapstruct.Mapper;

import com.example.entity.ProductVariantImage;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ManagerVariantImageMapper {
    
    ManagerVariantImageResponse toResponse(ProductVariantImage entity);

    ProductVariantImage toEntity(ManagerVariantImageRequest request);

    void updateEntity(ManagerVariantImageRequest request, @MappingTarget ProductVariantImage entity);
}
