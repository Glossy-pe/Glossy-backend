package com.example.service;

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
import com.example.enums.OrderStatus;
import com.example.repository.OrderRepository;
import com.example.repository.ProductVariantRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    public Page<Order> findAll(String q, List<String> statusList, Long variantId, Pageable pageable) {
        boolean emptyStatus = statusList == null || statusList.isEmpty();
        List<String> list = emptyStatus ? List.of("") : statusList;
        String cleanQ = q == null ? "" : q.trim();

        if (variantId != null) {
            return orderRepository.findAllByVariantId(variantId, list, emptyStatus, pageable);
        }

        return orderRepository.findAllByQuery(cleanQ, list, emptyStatus, pageable);
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
        // ✅ createdAt ya no se setea manualmente, Auditable lo hace solo

        List<OrderItem> orderItems = request.getOrderItems().stream().map(itemRequest -> {
            ProductVariant productVariant = productVariantRepository
                    .findById(itemRequest.getProductVariantId())
                    .orElseThrow();

            OrderItem orderItem = new OrderItem();
            orderItem.setProductVariant(productVariant);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPaidQuantity(itemRequest.getPaidQuantity());
            orderItem.setAmountPaid(itemRequest.getAmountPaid());
            orderItem.setSeparatedQuantity(itemRequest.getSeparatedQuantity());
            orderItem.setPackedQuantity(itemRequest.getPackedQuantity());
            orderItem.setOrder(order);

            if (request.getStatus() != OrderStatus.QUOTE) {
                if (productVariant.getStock() < itemRequest.getQuantity()) {
                    throw new RuntimeException("Stock insuficiente para el producto: " + productVariant.getId());
                }
                productVariant.setStock(productVariant.getStock() - itemRequest.getQuantity());
            }

            return orderItem;
        }).toList();

        order.setOrderItems(orderItems);
        return orderRepository.save(order);
    }

    @Transactional
    public Order update(Long id, OrderRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderStatus previousStatus = order.getStatus();
        OrderStatus newStatus = request.getStatus();

        boolean wasQuote = previousStatus == OrderStatus.QUOTE;
        boolean isNowQuote = newStatus == OrderStatus.QUOTE;

        if (!wasQuote) {
            order.getOrderItems().forEach(existingItem -> {
                ProductVariant variant = existingItem.getProductVariant();
                variant.setStock(variant.getStock() + existingItem.getQuantity());
                productVariantRepository.save(variant);
            });
        }

        order.setStatus(newStatus);
        order.setTotal(request.getTotal());
        order.setCostTotal(request.getCostTotal());
        order.setCustomerName(request.getCustomerName());
        order.setCustomerAddress(request.getCustomerAddress());
        // ✅ updatedAt se actualiza solo por @LastModifiedDate

        List<OrderItem> updatedItems = request.getOrderItems().stream().map(itemRequest -> {
            ProductVariant productVariant = productVariantRepository
                    .findById(itemRequest.getProductVariantId())
                    .orElseThrow(() -> new RuntimeException("ProductVariant not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setProductVariant(productVariant);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPaidQuantity(itemRequest.getPaidQuantity());
            orderItem.setAmountPaid(itemRequest.getAmountPaid());
            orderItem.setSeparatedQuantity(itemRequest.getSeparatedQuantity());
            orderItem.setPackedQuantity(itemRequest.getPackedQuantity());
            orderItem.setOrder(order);

            if (!isNowQuote) {
                if (productVariant.getStock() < itemRequest.getQuantity()) {
                    throw new RuntimeException("Stock insuficiente para el producto: " + productVariant.getId());
                }
                productVariant.setStock(productVariant.getStock() - itemRequest.getQuantity());
            }

            return orderItem;
        }).toList();

        order.getOrderItems().clear();
        order.getOrderItems().addAll(updatedItems);

        return orderRepository.save(order);
    }

    @Transactional
    public void delete(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.QUOTE) {
            order.getOrderItems().forEach(orderItem -> {
                ProductVariant productVariant = orderItem.getProductVariant();
                productVariant.setStock(productVariant.getStock() + orderItem.getQuantity());
                productVariantRepository.save(productVariant);
            });
        }

        orderRepository.deleteById(orderId);
    }
}