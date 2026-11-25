package com.example.dto.manager.response.box_presentation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ManagerBoxPresentationResponse {
    private Long productId;
    private Integer totalUnits;
    private int activeVariants;
    private Integer unitsPerVariant;  // null si el total no se divide exacto
    private boolean valid;            // true si se puede registrar BOX
}