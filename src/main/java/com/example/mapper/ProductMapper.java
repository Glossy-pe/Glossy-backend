package com.example.mapper;

import com.example.dtos.request.ProductRequest;
import com.example.dtos.request.ProductRequestV2;
import com.example.dtos.response.LabelResponse;
import com.example.dtos.response.ProductImageResponse;
import com.example.dtos.response.ProductResponse;
import com.example.dtos.response.ProductResponseV2;
import com.example.entity.Category;
import com.example.entity.Product;
import com.example.entity.ProductImage;
import com.example.entity.ProductLabel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
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

//    @Mapping(target = "category", source = "categoryId")
//    @Mapping(target = "productLabels", ignore = true)
//    @Mapping(target = "variants", ignore = true)
//    @Mapping(target = "images", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "productLabels", ignore = true)
    Product toEntityV2(ProductRequestV2 request);

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "variants", source = "variants") // Ahora se mapea automáticamente
    @Mapping(target = "images", source = "images")
    @Mapping(target = "labels", source = "productLabels")
    ProductResponseV2 toResponseV2(Product product);

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

    // 🔥 Método que convierte ProductLabel → LabelResponse
    default List<LabelResponse> mapProductLabels(List<ProductLabel> productLabels) {

        if (productLabels == null) {
            return new ArrayList<>();
        }

        return productLabels.stream()
                .map(pl -> {
                    LabelResponse lr = new LabelResponse();
                    lr.setId(pl.getLabel().getId());
                    lr.setName(pl.getLabel().getName());
                    return lr;
                })
                .toList();
    }
}