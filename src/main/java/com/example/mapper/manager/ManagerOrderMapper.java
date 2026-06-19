package com.example.mapper.manager;

import com.example.dto.manager.request.order.ManagerOrderRequest;
import com.example.dto.manager.response.order.ManagerOrderResponse;
import com.example.dto.manager.response.order.ManagerOrderResponseFull;
import com.example.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ManagerOrderMapper {

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    ManagerOrderResponse toResponse(Order entity);

    Order toEntity(ManagerOrderRequest request);

    @Mapping(target = "items", ignore = true)
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    ManagerOrderResponseFull toResponseFull(Order entity);

    void updateEntity(ManagerOrderRequest request, @MappingTarget Order product);
}
