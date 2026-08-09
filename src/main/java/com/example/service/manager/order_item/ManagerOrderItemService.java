package com.example.service.manager.order_item;

import com.example.dto.manager.request.order_item.ManagerOrderItemRequest;
import com.example.dto.manager.response.order_item.ManagerOrderItemResponse;
import com.example.dto.manager.response.order_item.ManagerOrderItemResponseFull;
import com.example.dto.manager.response.page.ManagerPageResponse;
import com.example.entity.OrderItem;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.manager.ManagerOrderItemMapper;
import com.example.repository.OrderItemRepository;
import com.example.repository.VariantRepository;
import com.example.service.manager.variant.ManagerVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.example.exception.DuplicateOrderItemException;

@Service
public class ManagerOrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private ManagerOrderItemMapper managerOrderItemMapper;

    @Autowired
    private ManagerVariantService managerVariantService;

    public Flux<ManagerOrderItemResponse> getAll() {
        return orderItemRepository.findAll().map(managerOrderItemMapper::toResponse);
    }

    public Mono<ManagerPageResponse<ManagerOrderItemResponseFull>> getAllFull(PageRequest pageable) {
        Mono<Long> totalElements = orderItemRepository.count();

        Flux<ManagerOrderItemResponseFull> content = orderItemRepository.findAllBy(pageable)
                .flatMap(item -> {
                    ManagerOrderItemResponseFull response = managerOrderItemMapper.toResponseFull(item);
                    return variantRepository.findVariantDetailById(item.getProductVariantId())
                            .doOnNext(response::setVariant)
                            .thenReturn(response);
                });

        return Mono.zip(content.collectList(), totalElements)
                .map(tuple -> ManagerPageResponse.of(tuple.getT1(), pageable.getPageNumber(), pageable.getPageSize(), tuple.getT2()));
    }

    public Mono<ManagerOrderItemResponse> getOrderItemById(Long id) {
        return orderItemRepository.findById(id)
                .map(managerOrderItemMapper::toResponse);
    }

    public Mono<ManagerPageResponse<ManagerOrderItemResponse>> getVariantByOrderId(Long id, PageRequest pageable) {
        Mono<Long> totalElements = orderItemRepository.countByOrderId(id);
        Flux<ManagerOrderItemResponse> content = orderItemRepository.findByOrderId(id, pageable)
                .map(managerOrderItemMapper::toResponse);

        return Mono.zip(content.collectList(), totalElements)
                .map(tuple -> ManagerPageResponse.of(tuple.getT1(), pageable.getPageNumber(), pageable.getPageSize(), tuple.getT2()));
    }

    @Transactional
    public Mono<ManagerOrderItemResponse> create(ManagerOrderItemRequest request) {
        return managerVariantService.deductStock(request.getProductVariantId(), request.getQuantity())
                .then(variantRepository.findById(request.getProductVariantId()))
                .flatMap(variant -> {
                    OrderItem entity = managerOrderItemMapper.toEntity(request);
//                    entity.setUnitPrice(variant.getPrice());
                    return orderItemRepository.save(entity);
                })
                .map(managerOrderItemMapper::toResponse);
    }

    @Transactional
    public Mono<ManagerOrderItemResponse> update(Long id, ManagerOrderItemRequest request) {
        return orderItemRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("OrderItem not found with id: " + id)))
                .flatMap(item -> {
                    int diff = request.getQuantity() - item.getQuantity();

                    Mono<Void> stockAdjustment;
                    if (diff > 0) {
                        stockAdjustment = managerVariantService.deductStock(item.getProductVariantId(), diff);
                    } else if (diff < 0) {
                        stockAdjustment = managerVariantService.restoreStock(item.getProductVariantId(), Math.abs(diff));
                    } else {
                        stockAdjustment = Mono.empty();
                    }

                    return stockAdjustment.then(Mono.defer(() -> {
                        managerOrderItemMapper.updateEntity(request, item);
                        return orderItemRepository.save(item);
                    }));
                })
                .map(managerOrderItemMapper::toResponse);
    }

    @Transactional
    public Mono<Void> delete(Long id) {
        return orderItemRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("OrderItem not found with id: " + id)))
                .flatMap(item ->
                        managerVariantService.restoreStock(item.getProductVariantId(), item.getQuantity())
                                .then(orderItemRepository.deleteById(id))
                );
    }

}
