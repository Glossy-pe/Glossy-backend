package com.example.service.manager.order_item;

import com.example.dto.manager.request.order_item.ManagerOrderItemRequest;
import com.example.dto.manager.response.order_item.ManagerOrderItemResponse;
import com.example.entity.OrderItem;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.manager.ManagerOrderItemMapper;
import com.example.repository.OrderItemRepository;
import com.example.repository.VariantRepository;
import com.example.service.manager.variant.ManagerVariantService;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Mono<ManagerOrderItemResponse> getOrderItemById(Long id) {
        return orderItemRepository.findById(id)
                .map(managerOrderItemMapper::toResponse);
    }

    public Flux<ManagerOrderItemResponse> getVariantByOrderId(Long id) {
        return orderItemRepository.findByOrderId(id)
                .map(managerOrderItemMapper::toResponse);
    }

    @Transactional
    public Mono<ManagerOrderItemResponse> create(ManagerOrderItemRequest request) {
        return orderItemRepository.findByOrderIdAndProductVariantId(request.getOrderId(), request.getProductVariantId())
                .flatMap(existing -> Mono.<ManagerOrderItemResponse>error(
                        new DuplicateOrderItemException(request.getProductVariantId())
                ))
                .switchIfEmpty(
                        managerVariantService.deductStock(request.getProductVariantId(), request.getQuantity())
                                .then(variantRepository.findById(request.getProductVariantId()))
                                .flatMap(variant -> {
                                    OrderItem entity = managerOrderItemMapper.toEntity(request);
                                    entity.setUnitPrice(variant.getPrice());
                                    return orderItemRepository.save(entity);
                                })
                                .map(managerOrderItemMapper::toResponse)
                );
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
