package com.example.service;

import com.example.entity.Category;
import com.example.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category save(Category category){
        return categoryRepository.save(category);
    }

    public Category findById(Long id){
        return categoryRepository.findById(id).orElseThrow();
    }

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    @Transactional
    public Category update(Long categoryId, Category updatedCategory) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        if (updatedCategory.getName() != null) {
            category.setName(updatedCategory.getName());
        }

        if (updatedCategory.getImage() != null) {
            category.setImage(updatedCategory.getImage());
        }

        return categoryRepository.save(category);
    }

    @Transactional
    public void delete(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        categoryRepository.delete(category);
    }
}
