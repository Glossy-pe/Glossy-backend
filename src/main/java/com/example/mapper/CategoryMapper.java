package com.example.mapper;

import com.example.dto.admin.response.CategoryResponse;
import com.example.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toResponse(Category category);
}
