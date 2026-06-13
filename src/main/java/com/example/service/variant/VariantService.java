package com.example.service.variant;

import com.example.dto.admin.request.variant.VariantRequest;
import com.example.dto.admin.response.variant.VariantResponse;
import com.example.entity.ProductVariant;
import com.example.mapper.VariantMapper;
import com.example.repository.VariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VariantService {

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private VariantMapper variantMapper;

    public Flux<VariantResponse> getAllVariants() {
        return variantRepository.findAll().map(variantMapper::toResponse);
    }

    public Mono<VariantResponse> getVariantById(Long id) {
        return variantRepository.findById(id)
                .map(variantMapper::toResponse);
    }

    public Flux<VariantResponse> getVariantByProductId(Long id) {
        return variantRepository.findByProductId(id)
                .map(variantMapper::toResponse);
    }

    public Mono<VariantResponse> create(VariantRequest request) {
        ProductVariant entity = variantMapper.toEntity(request);

        return variantRepository.save(entity)
                .map(variantMapper::toResponse);
    }

    public Mono<VariantResponse> update(Long id, VariantRequest request) {
        return variantRepository.findById(id)
                .flatMap(variant -> {

                    variantMapper.updateEntity(request, variant);

                    return variantRepository.save(variant);
                })
                .map(variantMapper::toResponse);
    }

    public Mono<Void> delete(Long id) {
        return variantRepository.deleteById(id);
    }
}
