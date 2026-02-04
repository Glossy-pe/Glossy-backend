package com.example.mapper;

import com.example.dtos.request.UserRequest;
import com.example.dtos.response.UserResponse;
import com.example.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);
    User toEntity(UserRequest userRequest);
}
