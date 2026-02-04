package com.example.mapper;

import com.example.dtos.request.ProductImageRequest;
import com.example.entity.Product;
import com.example.entity.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {

    ProductImage toEntity(ProductImageRequest request);

    default Product mapProduct(Long productId) {
        if (productId == null) return null;

        Product product = new Product();
        product.setId(productId);
        return product;
    }
}
