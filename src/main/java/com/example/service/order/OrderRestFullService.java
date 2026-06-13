package com.example.service.order;

import com.example.dto.admin.response.order.OrderResponseFull;
import com.example.dto.admin.response.page.PageResponse;
import com.example.mapper.*;
import com.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrderRestFullService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;

    public Mono<OrderResponseFull> getOrderByIdFull(Long id) {
        return orderRepository.findById(id)
                .flatMap(order ->
                        orderItemRepository.findByOrderId(order.getId())
                                .map(orderItemMapper::toResponse)  // mapea cada item
                                .collectList()
                                .map(orderItems -> {
                                    OrderResponseFull response = orderMapper.toResponseFull(order);
                                    response.setItems(orderItems);
                                    return response;
                                })
                );
    }

    // service
    public Mono<PageResponse<OrderResponseFull>> getAllOrdersFull(Pageable pageable) {
        return orderRepository.count()
                .flatMap(total ->
                        orderRepository.findAllBy(pageable)
                                .flatMap(order ->
                                                orderItemRepository.findByOrderId(order.getId())
                                                        .map(orderItemMapper::toResponse)
                                                        .collectList()
                                                        .map(orderItems -> {
                                                            OrderResponseFull response = orderMapper.toResponseFull(order);
                                                            response.setItems(orderItems);
                                                            return response;
                                                        }),
                                        5
                                )
                                .collectList()
                                .map(orders -> PageResponse.of(
                                        orders,
                                        pageable.getPageNumber(),
                                        pageable.getPageSize(),
                                        total
                                ))
                );
    }
}
