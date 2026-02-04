package com.example.mapper;

import com.example.dtos.request.CategoryRequest;
import com.example.dtos.response.CategoryResponse;
import com.example.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toResponse(Category category);
    Category toEntity(CategoryRequest categoryRequest);
}
