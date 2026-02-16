package com.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "product_label")
@AllArgsConstructor
@NoArgsConstructor
public class ProductLabel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "label_id", nullable = false)
    private Label label;

    public ProductLabel(Product product, Label label) {
        this.product = product;
        this.label = label;
    }

}