package com.example.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.dtos.response.*;
import com.example.entity.ProductVariantImage;
import com.example.dtos.request.*;

@Mapper(componentModel = "spring")
public interface VariantImageMapper {

    @Mapping(target = "variantId", source = "productVariant.id")
    VariantImageResponse toResponse(ProductVariantImage image);

    @Mapping(target = "productVariant.id", source = "variantId")
    @Mapping(target = "id", ignore = true)
    ProductVariantImage toEntity(VariantImageRequest request);
}