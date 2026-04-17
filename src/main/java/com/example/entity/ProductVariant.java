package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.example.events.VariantEntityListener;

@Entity
@Getter
@Setter
@Table(name = "product_variant")

@NoArgsConstructor
@EntityListeners(VariantEntityListener.class)
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(255) default 'Sin nombre'")
    private String toneName;

    @Column(nullable = false)
    private String toneCode;   // CL-01, MD-02

    @Column(nullable = true, precision = 10, scale = 2)
    private BigDecimal cost;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = true)
    private Integer position;

    @Column(nullable = true)
    private Boolean active = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToMany(mappedBy = "productVariant")
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariantImage> images = new ArrayList<>();
}
