package com.example.entity;

import com.example.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Table(name = "orders")

@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = true)
    private String customerName;

    @Column(nullable = true)
    private String customerAddress;

    @Column(nullable = false, unique = true, length = 20)
    private String orderCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private OrderStatus status;

    @Column(nullable = true)
    private BigDecimal costTotal;

    @Column(nullable = true)
    private BigDecimal total;

    @Column(nullable = true)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
}


