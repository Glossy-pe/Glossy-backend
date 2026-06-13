package com.example.service.product;

import com.example.dto.admin.request.product.ProductRequest;
import com.example.dto.admin.response.page.PageResponse;
import com.example.dto.admin.response.product.ProductResponse;
import com.example.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mapper.ProductMapper;
import com.example.repository.ProductRepository;
import org.springframework.data.domain.Pageable;

import reactor.core.publisher.Mono;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    public Mono<PageResponse<ProductResponse>> getAllProducts(Pageable pageable) {
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

    public Mono<ProductResponse> getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toResponse);
    }

    public Mono<ProductResponse> create(ProductRequest request) {
        Product entity = productMapper.toEntity(request);

        return productRepository.save(entity)
                .map(productMapper::toResponse);
    }

    public Mono<ProductResponse> update(Long id, ProductRequest request) {
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
