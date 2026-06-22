package com.example.mapper.guest;

import com.example.dto.guest.response.order_item.GuestOrderItemResponseFull;
import com.example.entity.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GuestOrderItemMapper {
    GuestOrderItemResponseFull toResponseFull(OrderItem orderItem);
}
