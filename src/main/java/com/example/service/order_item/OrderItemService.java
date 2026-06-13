package com.example.service.order_item;

import com.example.dto.admin.request.order_item.OrderItemRequest;
import com.example.dto.admin.response.order_item.OrderItemResponse;
import com.example.entity.OrderItem;
import com.example.mapper.OrderItemMapper;
import com.example.repository.OrderItemRepository;
import com.example.repository.VariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private OrderItemMapper orderItemMapper;

    public Flux<OrderItemResponse> getAll() {
        return orderItemRepository.findAll().map(orderItemMapper::toResponse);
    }

    public Mono<OrderItemResponse> getOrderItemById(Long id) {
        return orderItemRepository.findById(id)
                .map(orderItemMapper::toResponse);
    }

    public Flux<OrderItemResponse> getVariantByOrderId(Long id) {
        return orderItemRepository.findByOrderId(id)
                .map(orderItemMapper::toResponse);
    }

    public Mono<OrderItemResponse> create(OrderItemRequest request) {
        return variantRepository.findById(request.getProductVariantId())
                .flatMap(variant -> {
                    OrderItem entity = orderItemMapper.toEntity(request);
                    entity.setUnitPrice(variant.getPrice());
                    return orderItemRepository.save(entity);
                })
                .map(orderItemMapper::toResponse);
    }

    public Mono<OrderItemResponse> update(Long id, OrderItemRequest request) {
        return orderItemRepository.findById(id)
                .flatMap(variant -> {

                    orderItemMapper.updateEntity(request, variant);

                    return orderItemRepository.save(variant);
                })
                .map(orderItemMapper::toResponse);
    }

    public Mono<Void> delete(Long id) {
        return orderItemRepository.deleteById(id);
    }
}
