package com.example.mapper;

import org.mapstruct.Mapper;
import com.example.dtos.response.OrderItemResponse;
import com.example.entity.OrderItem;

@Mapper(componentModel = "spring", uses = { ProductVariantMapper.class })
public interface OrderItemMapper {
    OrderItemResponse toResponse(OrderItem orderItem);
}