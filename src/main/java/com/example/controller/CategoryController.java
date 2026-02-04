package com.example.controller;

import com.example.dtos.request.CategoryRequest;
import com.example.dtos.request.UserRequest;
import com.example.dtos.response.CategoryResponse;
import com.example.dtos.response.ProductResponse;
import com.example.dtos.response.UserResponse;
import com.example.mapper.CategoryMapper;
import com.example.mapper.UserMapper;
import com.example.service.CategoryService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryMapper categoryMapper;

    @GetMapping("/{id}")
    public CategoryResponse findById(@PathVariable("id") Long id){
        return categoryMapper.toResponse(categoryService.findById(id));
    }

    @PostMapping
    public CategoryResponse create(@RequestBody CategoryRequest categoryRequest) {
        return categoryMapper.toResponse(
                categoryService.save(categoryMapper.toEntity(categoryRequest))
        );
    }

    @GetMapping("")
    public List<CategoryResponse> findAll(){
        return categoryService.findAll().stream().map(categoryMapper::toResponse).toList();
    }
}
