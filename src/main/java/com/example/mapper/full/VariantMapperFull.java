package com.example.mapper.full;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.dtos.response.full.VariantResponseFull;
import com.example.entity.ProductVariant;
import com.example.mapper.VariantImageMapper;

@Mapper(
    componentModel = "spring",
    uses = { VariantImageMapper.class }
)
public interface VariantMapperFull {

    @Mapping(target = "images", source = "images")
    VariantResponseFull toResponseFull(ProductVariant variant);

}
