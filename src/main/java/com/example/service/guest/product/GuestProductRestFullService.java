package com.example.service.guest.product;

import com.example.dto.admin.response.page.PageResponse;
import com.example.dto.guest.response.image.GuestProductImageResponse;
import com.example.dto.guest.response.product.GuestProductResponseFull;
import com.example.dto.guest.response.variant.GuestVariantResponseFull;
import com.example.entity.Product;
import com.example.mapper.guest.GuestProductImageMapper;
import com.example.mapper.guest.GuestProductMapper;
import com.example.mapper.guest.GuestVariantImageMapper;
import com.example.mapper.guest.GuestVariantMapper;
import com.example.repository.ProductImageRepository;
import com.example.repository.ProductRepository;
import com.example.repository.VariantImageRepository;
import com.example.repository.VariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GuestProductRestFullService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private VariantImageRepository variantImageRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private GuestProductImageMapper productImageMapper;

    @Autowired
    private GuestProductMapper productMapper;
    @Autowired
    private GuestVariantMapper variantMapper;
    @Autowired
    private GuestVariantImageMapper variantImageMapper;

    public Mono<GuestProductResponseFull> getProductByIdFull(Long id) {
        return productRepository.findById(id)
                .flatMap(product ->
                        Mono.zip(
                                resolveImages(product.getId()),
                                resolveVariants(product.getId())
                        ).map(tuple -> {
                            GuestProductResponseFull response = productMapper.toResponseFull(product);
                            response.setImages(tuple.getT1());
                            response.setVariants(tuple.getT2());
                            return response;
                        })
                );
    }

    public Mono<PageResponse<GuestProductResponseFull>> getAllProductsFull(Pageable pageable, Long categoryId) {
        Mono<Long> totalMono = (categoryId != null)
                ? productRepository.countVisibleForGuestByCategory(categoryId)
                : productRepository.countVisibleForGuest();

        Flux<Product> productsFlux = (categoryId != null)
                ? productRepository.findAllVisibleForGuestByCategory(
                categoryId, pageable.getPageSize(), pageable.getOffset())
                : productRepository.findAllVisibleForGuest(
                pageable.getPageSize(), pageable.getOffset());

        return totalMono.flatMap((Long total) ->
                productsFlux
                        .flatMap(product ->
                                Mono.zip(
                                        resolveImages(product.getId()),
                                        resolveVariants(product.getId())
                                ).map(tuple -> {
                                    GuestProductResponseFull response = productMapper.toResponseFull(product);
                                    response.setImages(tuple.getT1());
                                    response.setVariants(tuple.getT2());
                                    return response;
                                })
                        )
                        .collectList()
                        .map((List<GuestProductResponseFull> products) ->
                                PageResponse.of(products, pageable.getPageNumber(), pageable.getPageSize(), total)
                        )
        );
    }

    public Flux<GuestProductResponseFull> searchProductsFull(String query, Long categoryId) {
        if (query == null || query.trim().isEmpty()) {
            return Flux.empty();
        }
        String normalizedQuery = query.trim();

        Flux<Product> productsFlux = (categoryId != null)
                ? productRepository.findVisibleForGuestByNameContainingAndCategory(normalizedQuery, categoryId)
                : productRepository.findVisibleForGuestByNameContaining(normalizedQuery);

        return productsFlux
                .flatMap(product ->
                        Mono.zip(
                                resolveImages(product.getId()),
                                resolveVariants(product.getId())
                        ).map(tuple -> {
                            GuestProductResponseFull response = productMapper.toResponseFull(product);
                            response.setImages(tuple.getT1());
                            response.setVariants(tuple.getT2());
                            return response;
                        })
                );
    }

    // ── Helpers privados, reutilizados por los 3 métodos públicos ─────

    private Mono<List<GuestProductImageResponse>> resolveImages(Long productId) {
        return productImageRepository.findByProductId(productId)
                .map(productImageMapper::toResponse)
                .collectList();
    }

    private Mono<List<GuestVariantResponseFull>> resolveVariants(Long productId) {
        return variantRepository.findByProductId(productId)
                .flatMap(variant ->
                        variantImageRepository.findByProductVariantId(variant.getId())
                                .map(variantImageMapper::toResponse)
                                .collectList()
                                .map(images -> {
                                    GuestVariantResponseFull variantResponse = variantMapper.toResponseFull(variant);
                                    variantResponse.setImages(images);
                                    return variantResponse;
                                })
                )
                .collectList();
    }
}