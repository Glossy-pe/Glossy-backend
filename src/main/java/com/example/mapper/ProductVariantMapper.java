package com.example.mapper;

import com.example.dtos.request.ProductVariantRequest;
import com.example.dtos.response.ProductVariantResponse;
import com.example.entity.ProductVariant;
import com.example.entity.ProductVariantImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    ProductVariant toEntity(ProductVariantRequest productVariantRequest);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "images", target = "mainImageUrl", qualifiedByName = "toMainImageUrl")
    ProductVariantResponse toResponse(ProductVariant variant);

    @Named("toMainImageUrl")
    default String toMainImageUrl(List<ProductVariantImage> images) {
        if (images == null || images.isEmpty()) return null;
        return images.stream()
                .filter(ProductVariantImage::getMainImage) // busca la imagen marcada como principal
                .findFirst()
                .map(ProductVariantImage::getUrl)
                .orElse(images.get(0).getUrl()); // fallback a la primera si ninguna es main
    }
}