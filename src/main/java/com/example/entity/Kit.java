package com.example.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Table("kit")
public class Kit {

    @Id
    private Long id;

    private String name;

    private Boolean active = true;

    private LocalDateTime createdAt;
}