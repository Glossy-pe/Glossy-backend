package com.example.mapper;

import com.example.dtos.request.ProductRequest;
import com.example.dtos.response.ProductImageResponse;
import com.example.dtos.response.ProductResponse;
import com.example.entity.Category;
import com.example.entity.Product;
import com.example.entity.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Comparator;
import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductVariantMapper.class})
public interface ProductMapper {

    @Mapping(target = "images", source = "images")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "variants", source = "variants") // Ahora se mapea automáticamente
    ProductResponse toResponse(Product product);

    @Mapping(target = "category", source = "categoryId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "variants", ignore = true)
    @Mapping(target = "images", ignore = true)
    Product toEntity(ProductRequest productRequest);

    // Mapeo de ProductImage a ProductImageResponse
    @Mapping(target = "productId", source = "product.id")
    ProductImageResponse toImageResponse(ProductImage productImage);

    // Mapeo de lista de imágenes con ordenamiento
    default List<ProductImageResponse> mapImages(List<ProductImage> images) {
        if (images == null || images.isEmpty()) {
            return List.of();
        }
        return images.stream()
                .sorted(Comparator.comparingInt(ProductImage::getPosition))
                .map(this::toImageResponse)
                .toList();
    }

    default Category mapCategory(Long categoryId) {
        if (categoryId == null) return null;

        Category category = new Category();
        category.setId(categoryId);
        return category;
    }
}