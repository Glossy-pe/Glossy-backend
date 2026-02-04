package com.example.dtos.request;

import lombok.*;

@Getter
@Setter
public class UserRequest {
    private String name;
    private String email;
    private String passwordHash;
}
