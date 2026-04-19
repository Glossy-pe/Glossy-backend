package com.example.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.dtos.request.OrderRequest;
import com.example.entity.Order;
import com.example.entity.OrderItem;
import com.example.entity.ProductVariant;
import com.example.repository.OrderRepository;
import com.example.repository.ProductVariantRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    public Page<Order> findAll(String q, List<String> statusList, Pageable pageable) {
        boolean emptyStatus = statusList == null || statusList.isEmpty();
        List<String> list = emptyStatus ? List.of("") : statusList;
        return orderRepository.findAllByQuery(
                q == null ? "" : q,
                list,
                emptyStatus,
                pageable
        );
    }

    public Order findById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow();
    }

    @Transactional
    public Order create(OrderRequest request) {
        Order order = new Order();

        order.setOrderCode("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setStatus(request.getStatus());
        order.setCustomerName(request.getCustomerName());
        order.setCustomerAddress(request.getCustomerAddress());
        order.setTotal(request.getTotal());
        order.setCostTotal(request.getCostTotal());
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> orderItems = request.getOrderItems().stream().map(itemRequest -> {
            ProductVariant productVariant = productVariantRepository
                    .findById(itemRequest.getProductVariantId())
                    .orElseThrow();

            if (productVariant.getStock() < itemRequest.getQuantity()) {
                throw new RuntimeException("Insufficient stock");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProductVariant(productVariant);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setSeparated(itemRequest.getSeparated());
            orderItem.setOrder(order);

            productVariant.setStock(
                    productVariant.getStock() - itemRequest.getQuantity());

            return orderItem;
        }).toList();

        order.setOrderItems(orderItems);

        return orderRepository.save(order);
    }

    @Transactional
    public Order update(Long id, OrderRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Restaurar stock de los items anteriores
        order.getOrderItems().forEach(existingItem -> {
            ProductVariant variant = existingItem.getProductVariant();
            variant.setStock(variant.getStock() + existingItem.getQuantity());
            productVariantRepository.save(variant);
        });

        // Actualizar campos básicos
        order.setStatus(request.getStatus());
        order.setTotal(request.getTotal());
        order.setCostTotal(request.getCostTotal());
        order.setCustomerName(request.getCustomerName());
        order.setCustomerAddress(request.getCustomerAddress());
        order.setCreatedAt(LocalDateTime.now());

        // Reemplazar items
        List<OrderItem> updatedItems = request.getOrderItems().stream().map(itemRequest -> {
            ProductVariant productVariant = productVariantRepository
                    .findById(itemRequest.getProductVariantId())
                    .orElseThrow(() -> new RuntimeException("ProductVariant not found"));

            if (productVariant.getStock() < itemRequest.getQuantity()) {
                throw new RuntimeException("Insufficient stock for variant: " + productVariant.getId());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProductVariant(productVariant);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setSeparated(itemRequest.getSeparated());
            orderItem.setOrder(order);

            productVariant.setStock(productVariant.getStock() - itemRequest.getQuantity());

            return orderItem;
        }).toList();

        order.getOrderItems().clear();
        order.getOrderItems().addAll(updatedItems);

        return orderRepository.save(order);
    }

    public void delete(Long orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow();

        order.getOrderItems().stream().map(
                orderItem -> {
                    ProductVariant productVariant = productVariantRepository
                            .findById(orderItem.getProductVariant().getId()).orElseThrow();
                    productVariant.setStock(productVariant.getStock() + orderItem.getQuantity());
                    return orderItem;
                }).toList();
        orderRepository.deleteById(orderId);
    }

}
