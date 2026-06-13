package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.admin.response.CategoryResponse;
import com.example.mapper.CategoryMapper;
import com.example.repository.CategoryRepository;

import reactor.core.publisher.Flux;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    public Flux<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().map(categoryMapper::toResponse);
    }
}
