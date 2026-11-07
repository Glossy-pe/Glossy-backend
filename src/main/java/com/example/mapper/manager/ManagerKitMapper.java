package com.example.mapper.manager;

import com.example.dto.manager.request.kit.ManagerKitRequest;
import com.example.dto.manager.response.kit.ManagerKitDetailResponse;
import com.example.dto.manager.response.kit.ManagerKitItemResponse;
import com.example.dto.manager.response.kit.ManagerKitResponse;
import com.example.entity.Kit;
import com.example.entity.KitItem;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ManagerKitMapper {

    ManagerKitResponse toResponse(Kit kit);

    @Mapping(target = "items", ignore = true)
    ManagerKitDetailResponse toDetailResponse(Kit kit);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Kit toEntity(ManagerKitRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(ManagerKitRequest request, @MappingTarget Kit kit);

    @Mapping(target = "productName", ignore = true)
    ManagerKitItemResponse toItemResponse(KitItem item);
}