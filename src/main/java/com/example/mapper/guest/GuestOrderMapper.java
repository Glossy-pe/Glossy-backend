package com.example.mapper.guest;

import com.example.dto.guest.response.order.GuestOrderResponseFull;
import com.example.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GuestOrderMapper {
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    GuestOrderResponseFull toResponseFull(Order entity);
}
