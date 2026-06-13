package com.example.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@Table("product")
public class Product extends SoftDeletable {

    @Id
    private Long id;

    private String name;

    private String description;

    private String fullDescription;

    private String label;

    private Boolean active = true;

    private Long categoryId;

//    private String slug;
}