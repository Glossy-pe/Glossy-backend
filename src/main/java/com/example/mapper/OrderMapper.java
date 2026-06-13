package com.example.mapper;

import com.example.dto.admin.request.order.OrderRequest;
import com.example.dto.admin.response.order.OrderResponse;
import com.example.dto.admin.response.order.OrderResponseFull;
import com.example.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponse toResponse(Order entity);

    Order toEntity(OrderRequest request);

    @Mapping(target = "items", ignore = true)
    OrderResponseFull toResponseFull(Order entity);

    void updateEntity(OrderRequest request, @MappingTarget Order product);
}
