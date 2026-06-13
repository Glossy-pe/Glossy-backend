package com.example.service.manager.product;

import com.example.dto.admin.response.page.PageResponse;
import com.example.dto.manager.request.product.ManagerProductRequest;
import com.example.dto.manager.response.product.ManagerProductResponse;
import com.example.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mapper.manager.ManagerProductMapper;
import com.example.repository.ProductRepository;
import org.springframework.data.domain.Pageable;

import reactor.core.publisher.Mono;

@Service
public class ManagerProductService {
    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ManagerProductMapper productMapper;

    public Mono<PageResponse<ManagerProductResponse>> getAllProducts(Pageable pageable) {
        return productRepository.count()
                .flatMap(total ->
                        productRepository.findAllBy(pageable)
                                .map(productMapper::toResponse)
                                .collectList()
                                .map(orders -> PageResponse.of(
                                        orders,
                                        pageable.getPageNumber(),
                                        pageable.getPageSize(),
                                        total
                                ))
                );
    }

    public Mono<ManagerProductResponse> getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toResponse);
    }

    public Mono<ManagerProductResponse> create(ManagerProductRequest request) {
        Product entity = productMapper.toEntity(request);

        return productRepository.save(entity)
                .map(productMapper::toResponse);
    }

    public Mono<ManagerProductResponse> update(Long id, ManagerProductRequest request) {
        return productRepository.findById(id)
                .flatMap(product -> {
                    productMapper.updateEntity(request, product);
                    return productRepository.save(product);
                })
                .map(productMapper::toResponse);
    }

    public Mono<Void> delete(Long id) {
        return productRepository.deleteById(id);
    }
}
