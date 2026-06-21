package com.example.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@Table("brand")
public class Brand {

    @Id
    private Long id;

    private String name;

    private String slug;

    private String logoUrl;

    private String description;

    private Boolean active = true;
}