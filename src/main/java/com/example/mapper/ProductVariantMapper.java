package com.example.mapper;

import com.example.dtos.request.ProductVariantRequest;
import com.example.dtos.response.ProductVariantResponse;
import com.example.entity.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {
    // Para convertir request a entidad
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true) // Se asigna manualmente en el servicio
    ProductVariant toEntity(ProductVariantRequest productVariantRequest);

    ProductVariantResponse toResponse(ProductVariant variant);
}
