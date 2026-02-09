package com.example.service;

import com.example.entity.Product;
import com.example.entity.ProductImage;
import com.example.entity.ProductVariant;
import com.example.repository.ProductImageRepository;
import com.example.repository.ProductRepository;
import com.example.repository.ProductVariantRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private CategoryService categoryService;

    public Product create(Product product) {
        if (product.getId() != null) {
            throw new IllegalArgumentException("El id debe ser nulo al crear");
        }
        return productRepository.save(product);
    }

    public Product findById(Long id){
        return productRepository.findById(id).orElseThrow();
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

    public void delete(Long id){
        this.productRepository.deleteById(id);
    }

    public List<Product> findAll(String label){

        if (label != null && !label.trim().isEmpty()) {
            return productRepository.findByLabel(label);
        }

        return productRepository.findAll();
    }

    @Transactional
    public ProductVariant createVariant(Long productId, ProductVariant productVariant) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        productVariant.setId(null); // aseguras creación
        productVariant.setProduct(product);
        product.getVariants().add(productVariant);
        productRepository.save(product);
        return productVariant; // ahora sí
    }

    public ProductVariant updateVariant(Long productId, Long variantId, ProductVariant updatedVariant) {
        ProductVariant variant = productVariantRepository
                .findByIdAndProductId(variantId, productId)
                .orElseThrow(() -> new RuntimeException("Variante no encontrada"));

        // Actualizas SOLO los campos editables
        variant.setPrice(updatedVariant.getPrice());
        variant.setStock(updatedVariant.getStock());
        variant.setToneName(updatedVariant.getToneName());
        variant.setToneCode(updatedVariant.getToneCode());

        return productVariantRepository.save(variant);
    }

    @Transactional
    public void deleteVariant(Long productId, Long variantId) {
        ProductVariant variant = productVariantRepository
                .findByIdAndProductId(variantId, productId)
                .orElseThrow(() -> new RuntimeException("Variante no encontrada"));

        Product product = variant.getProduct();
        product.getVariants().remove(variant);

        productVariantRepository.delete(variant);
    }

    public Product createImage(Long productId, ProductImage productImage) {
        Product product = productRepository.findById(productId).orElseThrow();
        productImage.setProduct(product);
        product.getImages().add(productImage);
        return productRepository.save(product);
    }

    public ProductImage updateImage(Long productId, Long imageId, ProductImage updatedImage) {
        ProductImage image = productImageRepository
                .findByIdAndProductId(imageId, productId)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));

        // Actualizas SOLO los campos editables
        image.setMainImage(updatedImage.isMainImage());
        image.setUrl(updatedImage.getUrl());
        image.setPosition(updatedImage.getPosition());

        return productImageRepository.save(image);
    }

    @Transactional
    public void deleteImage(Long productId, Long imageId) {
        ProductImage image = productImageRepository
                .findByIdAndProductId(imageId, productId)
                .orElseThrow(() -> new RuntimeException("Variante no encontrada"));

        Product product = image.getProduct();
        product.getImages().remove(image);

        productImageRepository.delete(image);
    }
}
