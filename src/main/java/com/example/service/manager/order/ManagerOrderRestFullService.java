package com.example.service.manager.order;

import com.example.dto.admin.response.order.OrderResponseFull;
import com.example.dto.admin.response.page.PageResponse;
import com.example.dto.manager.response.order.ManagerOrderResponseFull;
import com.example.dto.manager.response.order_item.ManagerOrderItemResponseFull;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.OrderItemMapper;
import com.example.mapper.OrderMapper;
import com.example.mapper.manager.ManagerOrderItemMapper;
import com.example.mapper.manager.ManagerOrderMapper;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.service.manager.variant.ManagerVariantRestFullService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class ManagerOrderRestFullService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ManagerOrderMapper managerOrderMapper;
    @Autowired
    private ManagerOrderItemMapper managerOrderItemMapper;
    @Autowired
    private ManagerVariantRestFullService managerVariantRestFullService;

    // service
    public Mono<ManagerOrderResponseFull> getOrderByIdFull(Long id) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Order not found with id: " + id)))
                .flatMap(order ->
                        orderItemRepository.findByOrderId(order.getId())
                                .flatMap(item -> {
                                    ManagerOrderItemResponseFull itemFull = managerOrderItemMapper.toResponseFull(item);
                                    return managerVariantRestFullService.getVariantDetail(item.getProductVariantId())
                                            .doOnNext(itemFull::setVariant)
                                            .onErrorResume(e -> Mono.empty())
                                            .thenReturn(itemFull);
                                })
                                .collectList()
                                .map(orderItems -> {
                                    BigDecimal total = orderItems.stream()
                                            .map(i -> i.getUnitPrice() != null
                                                    ? i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity()))
                                                    : BigDecimal.ZERO)
                                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                                    ManagerOrderResponseFull response = managerOrderMapper.toResponseFull(order);
                                    response.setItems(orderItems);
                                    response.setTotal(total);
                                    return response;
                                })
                );
    }
}
