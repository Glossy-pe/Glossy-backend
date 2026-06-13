package com.example.mapper;

import com.example.dto.admin.request.order_item.OrderItemRequest;
import com.example.dto.admin.response.order_item.OrderItemResponse;
import com.example.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemResponse toResponse(OrderItem entity);

    OrderItem toEntity(OrderItemRequest request);

//    @Mapping(target = "items", ignore = true)
//    OrderResponseFull toResponseFull(Order entity);

    void updateEntity(OrderItemRequest request, @MappingTarget OrderItem entity);
}
