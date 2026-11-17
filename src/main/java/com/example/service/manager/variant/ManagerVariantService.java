package com.example.service.manager.variant;

import com.example.dto.manager.request.variant.ManagerVariantBulkRequest;
import com.example.dto.manager.request.variant.ManagerVariantRequest;
import com.example.dto.manager.response.variant.ManagerVariantResponse;
import com.example.dto.manager.response.variant.ManagerVariantQueryProjectionResponse;
import com.example.entity.ProductVariant;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.manager.ManagerVariantMapper;
import com.example.repository.ProductRepository;
import com.example.repository.VariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class ManagerVariantService {

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private ProductRepository productRepository;


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

    @Transactional
    public Flux<ManagerVariantResponse> createBulk(ManagerVariantBulkRequest r) {
        if (r.getProductId() == null)
            return bad("productId es obligatorio");
        if (r.getCount() == null || r.getCount() < 1 || r.getCount() > 100)
            return bad("La cantidad debe estar entre 1 y 100");
        if (r.getCost() == null || r.getCost().signum() < 0
                || r.getPrice() == null || r.getPrice().signum() < 0)
            return bad("Costo y precio deben ser 0 o mayores");
        if (r.getStock() == null || r.getStock() < 0)
            return bad("El stock debe ser 0 o mayor");

        Long productId = r.getProductId();
        String toneCode = r.getToneCode() != null ? r.getToneCode() : "#000000";

        return productRepository.existsById(productId)
                .flatMapMany(exists -> {
                    if (!exists) {
                        return Flux.<ProductVariant>error(
                                new ResourceNotFoundException("Product not found with id: " + productId));
                    }
                    return variantRepository.findMaxPositionByProductId(productId)
                            .flatMapMany(max -> {
                                List<ProductVariant> list = new ArrayList<>();
                                for (int i = 1; i <= r.getCount(); i++) {
                                    int n = max + i;
                                    ProductVariant v = new ProductVariant();
                                    v.setProductId(productId);
                                    v.setToneName("#" + n);
                                    v.setToneCode(toneCode);
                                    v.setCost(r.getCost());
                                    v.setPrice(r.getPrice());
                                    v.setStock(r.getStock());
                                    v.setPosition(n);
                                    v.setActive(true);
                                    list.add(v);
                                }
                                return variantRepository.saveAll(list);
                            });
                })
                .map(managerVariantMapper::toResponse);
    }

    private Flux<ManagerVariantResponse> bad(String msg) {
        return Flux.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, msg));
    }
}
