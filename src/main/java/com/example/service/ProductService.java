package com.example.service;

import com.example.entity.Product;
import com.example.entity.ProductImage;
import com.example.entity.ProductVariant;
import com.example.repository.ProductImageRepository;
import com.example.repository.ProductRepository;
import com.example.repository.ProductVariantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private CategoryService categoryService;


    public Product findById(Long id){
        return productRepository.findById(id).orElseThrow();
    }

    public Product create(Product product) {
        if (product.getId() != null) {
            throw new IllegalArgumentException("El id debe ser nulo al crear");
        }
        return productRepository.save(product);
    }

    public Product update(Long id, Product product) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setFullDescription(product.getFullDescription());
        existing.setActive(product.isActive());
        existing.setLabel(product.getLabel());
        existing.setCategory(product.getCategory());

        return productRepository.save(existing);
    }

    public List<Product> findAll(String label){

        if (label != null && !label.trim().isEmpty()) {
            return productRepository.findByLabel(label);
        }

        return productRepository.findAll();
    }

    public Product addVariantToProduct(Long productId, ProductVariant productVariant){
        Product product = productRepository.findById(productId).orElseThrow();
        productVariant.setProduct(product);
        product.getVariants().add(productVariant);
        return productRepository.save(product);
    }

    public Product addImageToProduct(Long productId, ProductImage productImage) {
        Product product = productRepository.findById(productId).orElseThrow();
        productImage.setProduct(product);
        product.getImages().add(productImage);
        return productRepository.save(product);
    }

}
