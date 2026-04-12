package com.example.mapper.full;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.dtos.response.LabelResponse;
import com.example.dtos.response.ProductImageResponse;
import com.example.dtos.response.full.ProductResponseFull;
import com.example.entity.Product;
import com.example.entity.ProductImage;
import com.example.entity.ProductLabel;

@Mapper(
    componentModel = "spring",
    uses = { VariantMapperFull.class }
)
public interface ProductMapperFull {

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "variants", source = "variants")
    @Mapping(target = "labels", source = "productLabels")
    @Mapping(target = "label", ignore = true)
    @Mapping(target = "images", source = "images")
    ProductResponseFull toResponseFull(Product product);

    @Mapping(target = "productId", source = "product.id")
    ProductImageResponse toImageResponse(ProductImage productImage);

    default List<LabelResponse> mapLabels(List<ProductLabel> productLabels) {
        if (productLabels == null) return new ArrayList<>();
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
