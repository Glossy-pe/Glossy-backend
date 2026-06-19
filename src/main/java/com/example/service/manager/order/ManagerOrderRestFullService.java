package com.example.service.manager.order;

import com.example.dto.admin.response.order.OrderResponseFull;
import com.example.dto.admin.response.page.PageResponse;
import com.example.dto.manager.response.order.ManagerOrderResponseFull;
import com.example.dto.manager.response.order_item.ManagerOrderItemResponseFull;
import com.example.dto.manager.response.order_status.ManagerOrderStatusResponse;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.OrderItemMapper;
import com.example.mapper.OrderMapper;
import com.example.mapper.manager.ManagerOrderItemMapper;
import com.example.mapper.manager.ManagerOrderMapper;
import com.example.mapper.manager.ManagerOrderStatusMapper;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.repository.OrderStatusRepository;
import com.example.service.manager.variant.ManagerVariantRestFullService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ManagerOrderRestFullService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderStatusRepository orderStatusRepository;
    @Autowired
    private ManagerOrderMapper managerOrderMapper;
    @Autowired
    private ManagerOrderStatusMapper managerOrderStatusMapper;
    @Autowired
    private ManagerOrderItemMapper managerOrderItemMapper;
    @Autowired
    private ManagerVariantRestFullService managerVariantRestFullService;

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
                                .flatMap(items -> {
                                    ManagerOrderResponseFull response = managerOrderMapper.toResponseFull(order);
                                    response.setItems(items);
                                    response.setTotal(calculateTotal(items));
                                    applyOrderFlags(response, items);

                                    if (order.getOrderStatusId() == null) {
                                        response.setOrderStatus(null);
                                        return Mono.just(response);
                                    }

                                    return orderStatusRepository.findById(order.getOrderStatusId())
                                            .map(managerOrderStatusMapper::toResponse)
                                            .doOnNext(response::setOrderStatus)
                                            .thenReturn(response);
                                })
                );
    }

    public Mono<PageResponse<ManagerOrderResponseFull>> getAllOrdersFull(
            String name,
            Long variantId,
            Long orderStatusId,    // 👈
            Boolean isPaid,
            Boolean isSeparated,
            Boolean isPacked,
            Pageable pageable
    ) {
        return orderRepository.count(name, variantId, orderStatusId, isPaid, isSeparated, isPacked)   // 👈
                .flatMap(total ->
                        orderRepository.findAllBy(
                                        name,
                                        variantId,
                                        orderStatusId,   // 👈
                                        isPaid,
                                        isSeparated,
                                        isPacked,
                                        pageable.getPageSize(),
                                        pageable.getOffset()
                                )
                                .flatMap(order ->
                                        orderItemRepository.findByOrderId(order.getId())
                                                .flatMap(item -> {
                                                    ManagerOrderItemResponseFull itemFull =
                                                            managerOrderItemMapper.toResponseFull(item);

                                                    return managerVariantRestFullService
                                                            .getVariantDetail(item.getProductVariantId())
                                                            .doOnNext(itemFull::setVariant)
                                                            .onErrorResume(e -> Mono.empty())
                                                            .thenReturn(itemFull);
                                                })
                                                .collectList()
                                                .flatMap(items -> {
                                                    ManagerOrderResponseFull response =
                                                            managerOrderMapper.toResponseFull(order);

                                                    response.setItems(items);
                                                    response.setTotal(calculateTotal(items));
                                                    applyOrderFlags(response, items);

                                                    if (order.getOrderStatusId() == null) {
                                                        return Mono.just(response);
                                                    }

                                                    return orderStatusRepository
                                                            .findById(order.getOrderStatusId())
                                                            .map(managerOrderStatusMapper::toResponse)
                                                            .doOnNext(response::setOrderStatus)
                                                            .thenReturn(response);
                                                })
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

    private BigDecimal calculateTotal(List<ManagerOrderItemResponseFull> items) {
        return items.stream()
                .map(i -> i.getUnitPrice() != null
                        ? i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity()))
                        : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void applyOrderFlags(ManagerOrderResponseFull response, List<ManagerOrderItemResponseFull> items) {
        boolean paid = !items.isEmpty() && items.stream()
                .allMatch(i -> i.getPaidQuantity() >= i.getQuantity());

        boolean separated = !items.isEmpty() && items.stream()
                .allMatch(i -> i.getSeparatedQuantity() >= i.getQuantity());

        boolean packed = !items.isEmpty() && items.stream()
                .allMatch(i -> i.getPackedQuantity() >= i.getQuantity());

        response.setPaid(paid);
        response.setSeparated(separated);
        response.setPacked(packed);
    }
}