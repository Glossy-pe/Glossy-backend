package com.example.mapper.manager;

import com.example.dto.manager.request.order_item.ManagerOrderItemRequest;
import com.example.dto.manager.response.order_item.ManagerOrderItemResponse;
import com.example.dto.manager.response.order_item.ManagerOrderItemResponseFull;
import com.example.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ManagerOrderItemMapper {

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    ManagerOrderItemResponse toResponse(OrderItem entity);

    OrderItem toEntity(ManagerOrderItemRequest request);

    void updateEntity(ManagerOrderItemRequest request, @MappingTarget OrderItem entity);

    ManagerOrderItemResponseFull toResponseFull(OrderItem orderItem);

}
