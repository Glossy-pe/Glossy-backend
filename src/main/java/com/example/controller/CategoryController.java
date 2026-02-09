package com.example.controller;

import com.example.dtos.request.CategoryRequest;
import com.example.dtos.response.CategoryResponse;
import com.example.mapper.CategoryMapper;
import com.example.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryMapper categoryMapper;

    @GetMapping("/{categoryId}")
    public CategoryResponse findById(@PathVariable("categoryId") Long categoryId){
        return categoryMapper.toResponse(categoryService.findById(categoryId));
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

    // ✅ UPDATE (PATCH)
    @PatchMapping("/{categoryId}")
    public CategoryResponse update(@PathVariable Long categoryId, @RequestBody CategoryRequest categoryRequest) {
        return categoryMapper.toResponse(
                categoryService.update(categoryId, categoryMapper.toEntity(categoryRequest))
        );
    }

    // ✅ DELETE
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> delete(@PathVariable Long categoryId) {
        categoryService.delete(categoryId);
        return ResponseEntity.noContent().build(); // 204
    }
}
