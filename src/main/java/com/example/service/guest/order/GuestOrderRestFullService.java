package com.example.service.guest.order;

import com.example.dto.admin.response.page.PageResponse;
import com.example.dto.guest.response.order.GuestOrderResponseFull;
import com.example.dto.guest.response.order_item.GuestOrderItemResponseFull;
import com.example.dto.manager.response.order.ManagerOrderResponseFull;
import com.example.dto.manager.response.order_item.ManagerOrderItemResponseFull;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.guest.GuestOrderItemMapper;
import com.example.mapper.guest.GuestOrderMapper;
import com.example.mapper.guest.GuestOrderStatusMapper;
import com.example.mapper.manager.ManagerOrderItemMapper;
import com.example.mapper.manager.ManagerOrderMapper;
import com.example.mapper.manager.ManagerOrderStatusMapper;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.repository.OrderStatusRepository;
import com.example.service.guest.product.GuestProductRestFullService;
import com.example.service.guest.variant.GuestVariantRestFullService;
import com.example.service.manager.variant.ManagerVariantRestFullService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GuestOrderRestFullService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderStatusRepository orderStatusRepository;
    @Autowired
    private GuestOrderMapper managerOrderMapper;
    @Autowired
    private GuestOrderStatusMapper managerOrderStatusMapper;
    @Autowired
    private GuestOrderItemMapper managerOrderItemMapper;
    @Autowired
    private GuestVariantRestFullService managerVariantRestFullService;

    @Autowired
    private GuestProductRestFullService guestProductRestFullService;

    public Mono<GuestOrderResponseFull> getOrderByIdFull(Long id) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Order not found with id: " + id)))
                .flatMap(order ->
                        orderItemRepository.findByOrderId(order.getId())
                                .flatMap(item -> {
                                    GuestOrderItemResponseFull itemFull = managerOrderItemMapper.toResponseFull(item);
                                    return managerVariantRestFullService.getById(item.getProductVariantId())
                                            .flatMap(variant -> {
                                                itemFull.setVariant(variant);
                                                return guestProductRestFullService.getById(variant.getProductId())
                                                        .doOnNext(itemFull::setProduct)
                                                        .onErrorResume(e -> Mono.empty())
                                                        .thenReturn(itemFull);
                                            })
                                            .onErrorResume(e -> Mono.empty())
                                            .thenReturn(itemFull);
                                })
                                .collectList()
                                .flatMap(items -> {
                                    GuestOrderResponseFull response = managerOrderMapper.toResponseFull(order);
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


    private BigDecimal calculateTotal(List<GuestOrderItemResponseFull> items) {
        return items.stream()
                .map(i -> i.getUnitPrice() != null
                        ? i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity()))
                        : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void applyOrderFlags(GuestOrderResponseFull response, List<GuestOrderItemResponseFull> items) {
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

    public Mono<GuestOrderResponseFull> getOrderByToken(String token) {
        return orderRepository.findByPublicToken(token)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Order not found")))
                .flatMap(order -> {
                    if (order.getExpiresAt() == null || order.getExpiresAt().isBefore(LocalDateTime.now())) {
                        return Mono.error(new ResourceNotFoundException("Order link has expired or is inactive"));
                    }
                    return getOrderByIdFull(order.getId());
                });
    }
}
