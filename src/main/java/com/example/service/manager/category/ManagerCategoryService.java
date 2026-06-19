package com.example.service.manager.category;

import com.example.dto.manager.response.category.ManagerCategoryResponse;
import com.example.mapper.manager.ManagerCategoryMapper;
import com.example.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ManagerCategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ManagerCategoryMapper managerCategoryMapper;

    public Flux<ManagerCategoryResponse> getAllCategories() {
        return categoryRepository.findAll().map(managerCategoryMapper::toResponse);
    }
}
