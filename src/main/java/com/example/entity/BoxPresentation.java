package com.example.entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter @Setter @NoArgsConstructor
@Table("box_presentation")
public class BoxPresentation {
    @Id
    private Long id;
    private Long productId;
    private Integer totalUnits;
}