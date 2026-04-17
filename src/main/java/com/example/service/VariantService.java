package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Product;
import com.example.entity.ProductVariant;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.ProductRepository;
import com.example.repository.VariantRepository;

@Service
@Transactional(readOnly = true)
public class VariantService {

    private final ProductRepository productRepository;
    private final VariantRepository variantRepository;

    public VariantService(ProductRepository productRepository,
                          VariantRepository variantRepository) {
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
    }

    @Transactional
    public ProductVariant create(ProductVariant variant) {
        Product product = productRepository.findById(variant.getProduct().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con id: " + variant.getProduct().getId()));

        variant.setProduct(product);
        return variantRepository.save(variant);
    }

    public ProductVariant findById(Long variantId) {
        return variantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Variante no encontrada con id: " + variantId));
    }

    public List<ProductVariant> findByProductId(Long productId) {
        return variantRepository.findByProductId(productId);
    }

    @Transactional
    public ProductVariant update(Long variantId, ProductVariant updatedVariant) {
        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Variante no encontrada con id: " + variantId));

        variant.setPrice(updatedVariant.getPrice());
        variant.setCost(updatedVariant.getCost());
        variant.setPosition(updatedVariant.getPosition());
        variant.setStock(updatedVariant.getStock());
        variant.setActive(updatedVariant.getActive());
        variant.setToneName(updatedVariant.getToneName());
        variant.setToneCode(updatedVariant.getToneCode());

        return variantRepository.save(variant);
    }

    @Transactional
    public void delete(Long variantId) {
        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Variante no encontrada con id: " + variantId));

        variantRepository.delete(variant);
    }

}
