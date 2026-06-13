package com.example.repository;

import com.example.entity.Category;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CategoryRepository
        extends ReactiveCrudRepository<Category, Long> {
}