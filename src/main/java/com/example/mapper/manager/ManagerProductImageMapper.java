package com.example.mapper.manager;

import com.example.dto.manager.request.images.ManagerProductImageRequest;
import com.example.dto.manager.response.images.ManagerProductImageResponse;
import com.example.entity.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ManagerProductImageMapper {

    ManagerProductImageResponse toResponse(ProductImage entity);

    ProductImage toEntity(ManagerProductImageRequest request);

    void updateEntity(ManagerProductImageRequest request, @MappingTarget ProductImage entity);
}
