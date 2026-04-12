    package com.example.dtos.response;

    import lombok.Getter;
    import lombok.Setter;

    @Getter
    @Setter
    public class ProductImageResponse {
        private Long id;
        private String url;
        private int position;
        private Boolean mainImage;
        private Long productId;
    }
