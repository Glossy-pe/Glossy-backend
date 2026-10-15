package com.example.service.manager.variant;

import com.example.dto.manager.request.variant.ManagerVariantRequest;
import com.example.dto.manager.response.variant.ManagerVariantResponse;
import com.example.dto.manager.response.variant.ManagerVariantQueryProjectionResponse;
import com.example.entity.ProductVariant;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.manager.ManagerVariantMapper;
import com.example.repository.VariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ManagerVariantService {

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private ManagerVariantMapper managerVariantMapper;

    public Flux<ManagerVariantResponse> getAllVariants() {
        return variantRepository.findAll().map(managerVariantMapper::toResponse);
    }

    public Mono<ManagerVariantResponse> getVariantById(Long id) {
        return variantRepository.findById(id)
                .map(managerVariantMapper::toResponse);
    }

    public Flux<ManagerVariantResponse> getVariantByProductId(Long id) {
        return variantRepository.findByProductId(id)
                .map(managerVariantMapper::toResponse);
    }

    public Mono<ManagerVariantResponse> create(ManagerVariantRequest request) {
        ProductVariant entity = managerVariantMapper.toEntity(request);

        return variantRepository.save(entity)
                .map(managerVariantMapper::toResponse);
    }

    public Mono<Void> deductStock(Long variantId, int quantity) {
        return variantRepository.deductStock(variantId, quantity)
                .flatMap(rows -> {
                    if (rows > 0) return Mono.<Void>empty();
                    return variantRepository.existsById(variantId)
                            .flatMap(exists -> exists
                                    ? Mono.<Void>error(new IllegalStateException("Insufficient stock for variant id: " + variantId))
                                    : Mono.<Void>error(new ResourceNotFoundException("Variant not found with id: " + variantId)));
                });
    }

    public Mono<ManagerVariantResponse> update(Long id, ManagerVariantRequest request) {
        return variantRepository.findById(id)
                .flatMap(variant -> {

                    managerVariantMapper.updateEntity(request, variant);

                    return variantRepository.save(variant);
                })
                .map(managerVariantMapper::toResponse);
    }

    public Mono<Void> delete(Long id) {
        return variantRepository.deleteById(id);
    }

    public Mono<Void> restoreStock(Long variantId, int quantity) {
        return variantRepository.restoreStock(variantId, quantity)
                .flatMap(rows -> rows > 0
                        ? Mono.<Void>empty()
                        : Mono.<Void>error(new ResourceNotFoundException("Variant not found with id: " + variantId)));
    }

    public Mono<ManagerVariantQueryProjectionResponse> getVariantDetail(Long variantId) {
        return variantRepository.findVariantDetailById(variantId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Variant not found with id: " + variantId)));
    }
}
