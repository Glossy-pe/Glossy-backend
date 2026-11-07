package com.example.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@Table("kit_item")
public class KitItem {

    @Id
    private Long id;

    private Long kitId;

    private Long productId;

    private Integer position;
}