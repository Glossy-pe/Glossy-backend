package com.example.service.manager.product;

import com.example.dto.admin.response.page.PageResponse;
import com.example.dto.manager.response.images.ManagerProductImageResponse;
import com.example.dto.manager.response.product.ManagerProductResponseFull;
import com.example.dto.manager.response.variant.ManagerVariantResponseFull;
import com.example.entity.Product;
import com.example.mapper.manager.ManagerProductImageMapper;
import com.example.mapper.manager.ManagerProductMapper;
import com.example.mapper.manager.ManagerVariantImageMapper;
import com.example.mapper.manager.ManagerVariantMapper;
import com.example.repository.ProductImageRepository;
import com.example.repository.ProductRepository;
import com.example.repository.VariantImageRepository;
import com.example.repository.VariantRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ManagerProductRestFullService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private VariantImageRepository variantImageRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ManagerProductImageMapper productImageMapper;

    @Autowired
    private ManagerProductMapper productMapper;
    @Autowired
    private ManagerVariantMapper variantMapper;
    @Autowired
    private ManagerVariantImageMapper variantImageMapper;

    public Mono<ManagerProductResponseFull> getProductByIdFull(Long id) {
        return productRepository.findById(id)
                .flatMap(product ->
                        Mono.zip(
                                resolveImages(product.getId()),
                                resolveVariants(product.getId())
                        ).map(tuple -> {
                            ManagerProductResponseFull response = productMapper.toResponseFull(product);
                            response.setImages(tuple.getT1());
                            response.setVariants(tuple.getT2());
                            return response;
                        })
                );
    }

    public Mono<PageResponse<ManagerProductResponseFull>> getAllProductsFull(Pageable pageable) {
        return productRepository.count()
                .flatMap((Long total) ->
                        productRepository.findAllBy(pageable)
                                .flatMapSequential(product ->
                                        Mono.zip(
                                                resolveImages(product.getId()),
                                                resolveVariants(product.getId())
                                        ).map(tuple -> {
                                            ManagerProductResponseFull response = productMapper.toResponseFull(product);
                                            response.setImages(tuple.getT1());
                                            response.setVariants(tuple.getT2());
                                            return response;
                                        })
                                )
                                .collectList()
                                .map((List<ManagerProductResponseFull> products) ->
                                        PageResponse.of(
                                                products,
                                                pageable.getPageNumber(),
                                                pageable.getPageSize(),
                                                total
                                        )
                                )
                );
    }

    public Flux<ManagerProductResponseFull> searchProductsFull(String query) {
        if (query == null || query.trim().isEmpty()) {
            return Flux.empty();
        }
        String normalizedQuery = query.trim();

        return productRepository.findTop5ByNameContainingIgnoreCase(normalizedQuery)
                .flatMapSequential(product ->
                        Mono.zip(
                                resolveImages(product.getId()),
                                resolveVariants(product.getId())
                        ).map(tuple -> {
                            ManagerProductResponseFull response = productMapper.toResponseFull(product);
                            response.setImages(tuple.getT1());
                            response.setVariants(tuple.getT2());
                            return response;
                        })
                );
    }

    // ── Helpers privados, reutilizados por los 3 métodos públicos ─────

    private Mono<List<ManagerProductImageResponse>> resolveImages(Long productId) {
        return productImageRepository.findByProductId(productId)
                .map(productImageMapper::toResponse)
                .collectList();
    }

    private Mono<List<ManagerVariantResponseFull>> resolveVariants(Long productId) {
        return variantRepository.findByProductId(productId)
                .flatMap(variant ->
                        variantImageRepository.findByProductVariantId(variant.getId())
                                .map(variantImageMapper::toResponse)
                                .collectList()
                                .map(images -> {
                                    ManagerVariantResponseFull variantResponse = variantMapper.toResponseFull(variant);
                                    variantResponse.setImages(images);
                                    return variantResponse;
                                })
                )
                .collectList();
    }
}