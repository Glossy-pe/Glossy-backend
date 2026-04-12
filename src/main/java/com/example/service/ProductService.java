package com.example.service;

import com.example.dtos.request.product.ProductRequest;
import com.example.dtos.response.full.ProductResponseFull;
import com.example.dtos.response.product.ProductResponse;
import com.example.entity.*;
import com.example.mapper.ProductMapper;
import com.example.mapper.full.ProductMapperFull;
import com.example.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    /* */
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductMapperFull productMapperFull;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private ProductLabelRepository productLabelRepository;

    @Transactional(readOnly = true)
    public Page<ProductResponseFull> findAllFull(Pageable pageable, Long categoryId, Long labelId) {
        if (labelId != null) {
            return productRepository.findByProductLabels_Label_Id(labelId, pageable)
                    .map(productMapperFull::toResponseFull);
        }
        if (categoryId != null) {
            return productRepository.findByCategoryId(categoryId, pageable)
                    .map(productMapperFull::toResponseFull);
        }
        return productRepository.findAll(pageable)
                .map(productMapperFull::toResponseFull);
    }


    @Transactional(readOnly = true)
    public ProductResponseFull findByIdFull(Long id) {
        return productRepository.findById(id)
                .map(productMapperFull::toResponseFull)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    /* START: Product Controller V3 */

    @Transactional
    public ProductResponse create(ProductRequest productRequest) {
        Product product = productMapper.toEntity(productRequest);
        Product saved = productRepository.save(product);
        // ❌ Eliminar todo el bloque de imágenes
        return productMapper.toResponse(saved);
    }

    public ProductResponse findById(Long productId){
        return productMapper.toResponse(productRepository.findById(productId).orElseThrow());
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream().map(productMapper::toResponse).toList();
    }

    @Transactional
    public ProductResponse update(Long productId, ProductRequest productRequest) {
        Product existing = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        existing.setName(productRequest.getName());
        existing.setDescription(productRequest.getDescription());
        existing.setFullDescription(productRequest.getFullDescription());
        existing.setActive(productRequest.getActive());

        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        existing.setCategory(category);
        // ❌ Sin lógica de imágenes aquí
        return productMapper.toResponse(productRepository.save(existing));
    }

    public void delete(Long id){
        this.productRepository.deleteById(id);
    }

    public Page<ProductResponseFull> searchFull(String q, Pageable pageable) {
        if (q == null || q.isBlank()) {
            return productRepository.findAll(pageable)
                    .map(productMapperFull::toResponseFull);  // 👈 igual que findAllFull
        }
        return productRepository.findByNameContainingIgnoreCase(q, pageable)
                .map(productMapperFull::toResponseFull);      // 👈 convierte Product → ProductResponseFull
    }

    @Transactional
    public void updateProductLabels(Long productId, List<Long> labelIds) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        productLabelRepository.deleteByProductId(productId);

        if (labelIds != null && !labelIds.isEmpty()) {
            List<ProductLabel> newLabels = labelIds.stream()
                .map(labelId -> {
                    Label label = labelRepository.findById(labelId)
                        .orElseThrow(() -> new RuntimeException("Label no encontrado: " + labelId));
                    return new ProductLabel(product, label);
                })
                .collect(Collectors.toList());

            productLabelRepository.saveAll(newLabels);
        }
    }
}
