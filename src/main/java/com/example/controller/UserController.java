package com.example.controller;

import com.example.dtos.request.UserRequest;
import com.example.dtos.response.UserResponse;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }


    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable("id") Long id){
        return userMapper.toResponse(userService.findById(id));
    }

    @PostMapping
    public UserResponse create(@RequestBody UserRequest userRequest) {
        return userMapper.toResponse(
                userService.save(userMapper.toEntity(userRequest))
        );
    }


}
