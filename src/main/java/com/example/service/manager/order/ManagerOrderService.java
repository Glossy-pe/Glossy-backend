package com.example.service.manager.order;

import com.example.dto.admin.request.order.OrderRequest;
import com.example.dto.admin.response.order.OrderResponse;
import com.example.dto.admin.response.page.PageResponse;
import com.example.dto.manager.request.order.ManagerOrderRequest;
import com.example.dto.manager.response.order.ManagerOrderResponse;
import com.example.entity.Order;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.OrderMapper;
import com.example.mapper.manager.ManagerOrderMapper;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.service.manager.variant.ManagerVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class ManagerOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ManagerVariantService managerVariantService;

    @Autowired
    private ManagerOrderMapper managerOrderMapper;

//    public Mono<PageResponse<ManagerOrderResponse>> getAllOrders(
//            String name,
//            Pageable pageable
//    ) {
//        return orderRepository.count(name)
//                .flatMap(total ->
//                        orderRepository.findAllBy(
//                                        name,
//                                        pageable.getPageSize(),
//                                        pageable.getOffset()
//                                )
//                                .map(managerOrderMapper::toResponse)
//                                .collectList()
//                                .map(orders -> PageResponse.of(
//                                        orders,
//                                        pageable.getPageNumber(),
//                                        pageable.getPageSize(),
//                                        total
//                                ))
//                );
//    }

    public Mono<ManagerOrderResponse> getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(managerOrderMapper::toResponse);
    }

    public Mono<ManagerOrderResponse> create(ManagerOrderRequest request) {
        Order entity = managerOrderMapper.toEntity(request);
        entity.setOrderCode(generateOrderCode());
        entity.setPublicToken(UUID.randomUUID().toString());

        return orderRepository.save(entity)
                .map(managerOrderMapper::toResponse);
    }

    public Mono<ManagerOrderResponse> update(Long id, ManagerOrderRequest request) {
        return orderRepository.findById(id)
                .flatMap(order -> {

                    managerOrderMapper.updateEntity(request, order);

                    return orderRepository.save(order);
                })
                .map(managerOrderMapper::toResponse);
    }

    @Transactional
    public Mono<Void> delete(Long id) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Order not found with id: " + id)))
                .flatMap(order ->
                        orderItemRepository.findByOrderId(id)
                                .flatMap(item -> managerVariantService.restoreStock(item.getProductVariantId(), item.getQuantity()))
                                .then(orderItemRepository.deleteAllByOrderId(id))
                                .then(orderRepository.deleteById(id))
                );
    }


    private String generateOrderCode() {
        String hex = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return "ORD-" + hex;
    }
}
