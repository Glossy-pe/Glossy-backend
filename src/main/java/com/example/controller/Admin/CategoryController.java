package com.example.controller.Admin;

import com.example.dto.admin.response.CategoryResponse;
import com.example.service.CategoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public Flux<CategoryResponse> getAll() {
        return categoryService.getAllCategories();
    }
}
