package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table("users")
@NoArgsConstructor
@AllArgsConstructor
public class User extends SoftDeletable {

    @Id
    private Long id;

    private String name;

    private String email;

    private String passwordHash;
}