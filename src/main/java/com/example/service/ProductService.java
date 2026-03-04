package com.example.service;

import com.example.dtos.request.ProductRequestV2;
import com.example.dtos.request.ProductVariantRequest;
import com.example.entity.*;
import com.example.mapper.ProductImageMapper;
import com.example.mapper.ProductMapper;
import com.example.mapper.ProductVariantMapper;
import com.example.repository.LabelRepository;
import com.example.repository.OrderItemRepository;
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
    private OrderItemRepository orderItemRepository;
    @Autowired
    private LabelRepository labelRepository;

    /* */
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductImageMapper productImageMapper;
    @Autowired
    private ProductVariantMapper productVariantMapper;
    /* */

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

    public List<Product> findAll(Long labelId, Long categoryId) {

        if (labelId != null) {
            return productRepository.findByLabelId(labelId);
        }

        if (categoryId != null) {
            return productRepository.findByCategoryId(categoryId);
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

    @Transactional
    public void updateProductLabels(Long productId, List<Long> labelIds) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Elimina todas las relaciones actuales
        product.getProductLabels().clear();

        if (labelIds == null || labelIds.isEmpty()) {
            return; // Si viene vacío, queda sin labels
        }

        for (Long labelId : labelIds) {

            Label label = labelRepository.findById(labelId)
                    .orElseThrow(() -> new RuntimeException("Label no encontrado"));

            ProductLabel pl = new ProductLabel(product, label);
            product.getProductLabels().add(pl);
        }
    }

    /* DEMO VERSION 2*/
    public List<Product> findByLabelId(Long labelId){
        if (labelId != null) {
            return productRepository.findByLabelId(labelId);
        }
        return productRepository.findAll();
    }

    public List<Product> findByCategoryId(Long categoryId){
        if (categoryId != null) {
            return productRepository.findByCategoryId(categoryId);
        }
        return productRepository.findAll();
    }

@Transactional
public Product createV2(ProductRequestV2 productRequestV2){

    Product product = productMapper.toEntityV2(productRequestV2);

    product.setCategory(
            categoryService.findById(productRequestV2.getCategoryId())
    );

    product.getImages().forEach(img -> img.setProduct(product));
    product.getVariants().forEach(variant -> variant.setProduct(product));

    if (productRequestV2.getLabelsIds() != null) {
        productRequestV2.getLabelsIds().forEach(id -> {
            Label label = labelRepository.findById(id).orElseThrow();
            product.getProductLabels().add(new ProductLabel(product, label));
        });
    }

    return productRepository.save(product);
}

@Transactional
public Product updateV2(Long productId, ProductRequestV2 productRequestV2){

    Product product = productRepository.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

    // Campos simples
    product.setName(productRequestV2.getName());
    product.setDescription(productRequestV2.getDescription());
    product.setFullDescription(productRequestV2.getFullDescription());
    product.setActive(productRequestV2.isActive());
    product.setCategory(categoryService.findById(productRequestV2.getCategoryId()));

    // Images: reemplazar limpio (no tienen FKs externas)
    product.getImages().clear();
    if (productRequestV2.getImages() != null) {
        productRequestV2.getImages().forEach(imgReq -> {
            ProductImage img = productImageMapper.toEntity(imgReq);
            img.setProduct(product);
            product.getImages().add(img);
        });
    }

    // Variants
    if (productRequestV2.getVariants() != null) {
        List<Long> incomingIds = productRequestV2.getVariants().stream()
                .filter(v -> v.getId() != null)
                .map(ProductVariantRequest::getId)
                .toList();

        // Eliminar solo las que no vienen, validando que no estén en órdenes
        product.getVariants().removeIf(existing -> {
            boolean shouldRemove = !incomingIds.contains(existing.getId());
            if (shouldRemove && orderItemRepository.existsByProductVariantId(existing.getId())) {
                throw new RuntimeException(
                    "La variante '" + existing.getToneName() + "' está asociada a una orden y no puede eliminarse"
                );
            }
            return shouldRemove;
        });

        // Actualizar existentes y agregar nuevas
        productRequestV2.getVariants().forEach(varReq -> {
            if (varReq.getId() != null) {
                product.getVariants().stream()
                        .filter(v -> v.getId().equals(varReq.getId()))
                        .findFirst()
                        .ifPresent(v -> {
                            v.setToneName(varReq.getToneName());
                            v.setToneCode(varReq.getToneCode());
                            v.setPrice(varReq.getPrice());
                            v.setStock(varReq.getStock());
                        });
            } else {
                ProductVariant newVariant = productVariantMapper.toEntity(varReq);
                newVariant.setProduct(product);
                product.getVariants().add(newVariant);
            }
        });
    }

    // Labels: reemplazar limpio (no tienen FKs externas)
    product.getProductLabels().clear();
    if (productRequestV2.getLabelsIds() != null) {
        productRequestV2.getLabelsIds().forEach(labelId -> {
            Label label = labelRepository.findById(labelId).orElseThrow();
            product.getProductLabels().add(new ProductLabel(product, label));
        });
    }

    return productRepository.save(product);
}
}
