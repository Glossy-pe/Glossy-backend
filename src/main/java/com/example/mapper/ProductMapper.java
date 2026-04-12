package com.example.mapper;

import com.example.dtos.request.product.ProductRequest;
import com.example.dtos.response.LabelResponse;
import com.example.dtos.response.ProductImageResponse;
import com.example.dtos.response.product.ProductResponse;
import com.example.entity.Category;
import com.example.entity.Product;
import com.example.entity.ProductImage;
import com.example.entity.ProductLabel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductVariantMapper.class})
public interface ProductMapper {

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "labels", source = "productLabels")
    ProductResponse toResponse(Product product);

   @Mapping(target = "category", source = "categoryId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "variants", ignore = true)
    @Mapping(target = "label", ignore = true)        // 👈
    @Mapping(target = "productLabels", ignore = true) // 👈
    @Mapping(target = "images", ignore = true)
    Product toEntity(ProductRequest productRequest);

    // Mapeo de ProductImage a ProductImageResponse
    @Mapping(target = "productId", source = "product.id")
    ProductImageResponse toImageResponse(ProductImage productImage);

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

        return new ArrayList<>(productLabels.stream()
                .map(pl -> {
                    LabelResponse lr = new LabelResponse();
                    lr.setId(pl.getLabel().getId());
                    lr.setName(pl.getLabel().getName());
                    return lr;
                })
                .toList()); // 👈 envuélvelo en new ArrayList<>()
    }
}