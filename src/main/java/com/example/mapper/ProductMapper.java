package com.example.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.dto.admin.request.product.ProductRequest;
import com.example.dto.admin.response.product.ProductResponse;
import com.example.dto.admin.response.product.ProductResponseFull;
import com.example.entity.Product;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse toResponse(Product product);

    Product toEntity(ProductRequest request);

    @Mapping(target = "variants", ignore = true)
    ProductResponseFull toResponseFull(Product product);

    void updateEntity(ProductRequest request, @MappingTarget Product product);
}