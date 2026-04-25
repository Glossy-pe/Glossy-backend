package com.example.dtos.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDocument {
    private Long id;
    private String name;
    private String description;
    private String fullDescription;
    private Boolean active;
    private Long categoryId;
    private List<String> labelNames;   // para filtrar/buscar por label
    private List<Long> labelIds;       // para filtrar por ID
    private BigDecimal minPrice;       // precio mínimo entre variantes
    private BigDecimal maxPrice;
    private Integer totalStock;
}