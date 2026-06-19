package com.example.service.order;

import com.example.dto.admin.request.order.OrderRequest;
import com.example.dto.admin.response.order.OrderResponse;
import com.example.dto.admin.response.page.PageResponse;
import com.example.entity.Order;
import com.example.mapper.OrderMapper;
import com.example.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

//    public Mono<PageResponse<OrderResponse>> getAllOrders(
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
//                                .map(orderMapper::toResponse)
//                                .collectList()
//                                .map(orders -> PageResponse.of(
//                                        orders,
//                                        pageable.getPageNumber(),
//                                        pageable.getPageSize(),
//                                        total
//                                ))
//                );
//    }

    public Mono<OrderResponse> getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::toResponse);
    }

    public Mono<OrderResponse> create(OrderRequest request) {
        Order entity = orderMapper.toEntity(request);

        return orderRepository.save(entity)
                .map(orderMapper::toResponse);
    }

    public Mono<OrderResponse> update(Long id, OrderRequest request) {
        return orderRepository.findById(id)
                .flatMap(order -> {

                    orderMapper.updateEntity(request, order);

                    return orderRepository.save(order);
                })
                .map(orderMapper::toResponse);
    }

    public Mono<Void> delete(Long id) {
        return orderRepository.deleteById(id);
    }
}
