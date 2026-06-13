package com.example.mapper.manager;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.dto.manager.request.product.ManagerProductRequest;
import com.example.dto.manager.response.product.ManagerProductResponse;
import com.example.dto.manager.response.product.ManagerProductResponseFull;
import com.example.entity.Product;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ManagerProductMapper {

    ManagerProductResponse toResponse(Product product);

    Product toEntity(ManagerProductRequest request);

    @Mapping(target = "variants", ignore = true)
    ManagerProductResponseFull toResponseFull(Product product);

    void updateEntity(ManagerProductRequest request, @MappingTarget Product product);
}