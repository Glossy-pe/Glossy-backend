package com.example.mapper.manager;

import com.example.dto.manager.response.order_status.ManagerOrderStatusResponse;
import com.example.entity.OrderStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ManagerOrderStatusMapper {

    ManagerOrderStatusResponse toResponse(OrderStatus entity);
}
