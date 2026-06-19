package com.example.mapper.manager;

import com.example.dto.manager.response.category.ManagerCategoryResponse;
import com.example.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ManagerCategoryMapper {

    ManagerCategoryResponse toResponse(Category category);
}
