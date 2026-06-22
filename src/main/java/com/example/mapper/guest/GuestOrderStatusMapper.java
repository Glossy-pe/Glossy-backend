package com.example.mapper.guest;

import com.example.dto.guest.response.order_status.GuestOrderStatusResponse;
import com.example.entity.OrderStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GuestOrderStatusMapper {
    GuestOrderStatusResponse toResponse(OrderStatus entity);
}
