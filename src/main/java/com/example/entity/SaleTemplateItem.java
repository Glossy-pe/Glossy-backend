package com.example.entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter @Setter @NoArgsConstructor
@Table("sale_template_item")
public class SaleTemplateItem {
    @Id
    private Long id;
    private Long templateId;
    private Long variantId;
    private Integer quantity;
}