package com.example.service.manager.product;

import com.example.dto.admin.response.page.PageResponse;
import com.example.dto.manager.response.product.ManagerProductResponseFull;
import com.example.dto.manager.response.variant.ManagerVariantResponseFull;
import com.example.mapper.manager.ManagerProductMapper;
import com.example.mapper.manager.ManagerVariantImageMapper;
import com.example.mapper.manager.ManagerVariantMapper;
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
    private ManagerProductMapper productMapper;
    @Autowired
    private ManagerVariantMapper variantMapper;
    @Autowired
    private ManagerVariantImageMapper variantImageMapper;

    public Mono<ManagerProductResponseFull> getProductByIdFull(Long id) {
        return productRepository.findById(id)
                .flatMap(product ->
                        variantRepository.findByProductId(product.getId())
                                .flatMap(variant ->                          // ← flatMap en vez de map
                                        variantImageRepository.findByProductVariantId(variant.getId())
                                                .map(variantImageMapper::toResponse)
                                                .collectList()
                                                .map(images -> {
                                                    ManagerVariantResponseFull variantResponse = variantMapper.toResponseFull(variant);
                                                    variantResponse.setImages(images);
                                                    return variantResponse;
                                                })
                                )
                                .collectList()
                                .map(variants -> {
                                    ManagerProductResponseFull response = productMapper.toResponseFull(product);
                                    response.setVariants(variants);
                                    return response;
                                })
                );
    }

    public Mono<PageResponse<ManagerProductResponseFull>> getAllProductsFull(Pageable pageable) {
        return productRepository.count()
                .flatMap((Long total) ->
                        productRepository.findAllBy(pageable)
                                .flatMap(product ->
                                        variantRepository.findByProductId(product.getId())
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
                                                .collectList()
                                                .map(variants -> {
                                                    ManagerProductResponseFull response = productMapper.toResponseFull(product);
                                                    response.setVariants(variants);
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

        return productRepository.findByNameContainingIgnoreCase(normalizedQuery)
                .flatMap(product ->
                        variantRepository.findByProductId(product.getId())
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
                                .collectList()
                                .map(variants -> {
                                    ManagerProductResponseFull response = productMapper.toResponseFull(product);
                                    response.setVariants(variants);
                                    return response;
                                })
                );
    }
}
