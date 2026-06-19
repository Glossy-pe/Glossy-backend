package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("product_variant_image")
public class ProductVariantImage {

    @Id
    private Long id;

    private String url;

    private int position;

    private Boolean mainImage;

    private Long productVariantId;

    private String resourceType;
}