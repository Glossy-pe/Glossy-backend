package com.example.controller.Manager;

import com.example.dto.manager.response.category.ManagerCategoryResponse;
import com.example.service.manager.category.ManagerCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/manager/categories")
@RequiredArgsConstructor
public class ManagerCategoryController {
    private final ManagerCategoryService managerCategoryService;

    @GetMapping
    public Flux<ManagerCategoryResponse> getAll() {
        return managerCategoryService.getAllCategories();
    }
}
