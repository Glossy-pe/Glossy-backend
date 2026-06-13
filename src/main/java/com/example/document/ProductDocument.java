package com.example.document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductDocument {

    private Long id;
    private String name;
    private String description;
    private String label;
//    private String slug;
    private Boolean active;
    private Long categoryId;
    private List<String> keywords;
    private String normalizedName;
    private List<String> normalizedKeywords;
}