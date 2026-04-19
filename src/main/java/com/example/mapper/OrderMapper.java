package com.example.mapper;

import org.mapstruct.Mapper;

import com.example.dtos.request.OrderRequest;
import com.example.dtos.response.OrderResponse;
import com.example.entity.Order;

@Mapper(componentModel = "spring",
    uses = { ProductVariantMapper.class, OrderItemMapper.class })
public interface OrderMapper {
    OrderResponse toResponse(Order order);
    Order toEntity(OrderRequest request);
}