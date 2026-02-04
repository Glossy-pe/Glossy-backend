package com.example.service;

import com.example.entity.Product;
import com.example.entity.ProductVariant;
import com.example.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductVariantService {

    @Autowired
    private ProductVariantRepository productVariantRepository;

    public ProductVariant save(ProductVariant productVariant) {
        return productVariantRepository.save(productVariant);
    }
}
