package com.example.service.product;

import com.example.dto.admin.response.product.ProductResponseFull;
import com.example.dto.admin.response.variant.VariantResponseFull;
import com.example.mapper.ProductMapper;
import com.example.mapper.VariantImageMapper;
import com.example.mapper.VariantMapper;
import com.example.repository.ProductRepository;
import com.example.repository.VariantImageRepository;
import com.example.repository.VariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductRestFullService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private VariantImageRepository variantImageRepository;

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private VariantMapper variantMapper;
    @Autowired
    private VariantImageMapper variantImageMapper;

    public Mono<ProductResponseFull> getProductByIdFull(Long id) {
        return productRepository.findById(id)
                .flatMap(product ->
                        variantRepository.findByProductId(product.getId())
                                .flatMap(variant ->                          // ← flatMap en vez de map
                                        variantImageRepository.findByProductVariantId(variant.getId())
                                                .map(variantImageMapper::toResponse)
                                                .collectList()
                                                .map(images -> {
                                                    VariantResponseFull variantResponse = variantMapper.toResponseFull(variant);
                                                    variantResponse.setImages(images);
                                                    return variantResponse;
                                                })
                                )
                                .collectList()
                                .map(variants -> {
                                    ProductResponseFull response = productMapper.toResponseFull(product);
                                    response.setVariants(variants);
                                    return response;
                                })
                );
    }

    public Flux<ProductResponseFull> getAllProductsFull() {
        return productRepository.findAll()
                .flatMap(product ->
                        variantRepository.findByProductId(product.getId())
                                .flatMap(variant ->                          // ← flatMap en vez de map
                                        variantImageRepository.findByProductVariantId(variant.getId())
                                                .map(variantImageMapper::toResponse)
                                                .collectList()
                                                .map(images -> {
                                                    VariantResponseFull variantResponse = variantMapper.toResponseFull(variant);
                                                    variantResponse.setImages(images);
                                                    return variantResponse;
                                                })
                                )
                                .collectList()
                                .map(variants -> {
                                    ProductResponseFull response = productMapper.toResponseFull(product);
                                    response.setVariants(variants);
                                    return response;
                                })
                );
    }

}
