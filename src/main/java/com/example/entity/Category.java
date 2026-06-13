package com.example.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("category")
public class Category extends SoftDeletable {

    @Id
    private Long id;

    private String name;

    private String image;
}