package com.example.controller;

import com.example.dtos.request.ProductImageRequest;
import com.example.dtos.request.ProductRequest;
import com.example.dtos.request.ProductVariantRequest;
import com.example.dtos.response.ProductImageResponse;
import com.example.dtos.response.ProductResponse;
import com.example.dtos.response.ProductVariantResponse;
import com.example.entity.Product;
import com.example.entity.ProductImage;
import com.example.entity.ProductVariant;
import com.example.mapper.ProductImageMapper;
import com.example.mapper.ProductMapper;
import com.example.mapper.ProductVariantMapper;
import com.example.service.CategoryService;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductVariantMapper productVariantMapper;
    @Autowired
    private ProductImageMapper productImageMapper;



    @PostMapping
    public ProductResponse create(@RequestBody ProductRequest productRequest) {
        Product product = productMapper.toEntity(productRequest);
        return productMapper.toResponse(productService.create(product));
    }

    @GetMapping("/{productId}")
    public ProductResponse findById(@PathVariable("productId") Long productId){
        ProductResponse productResponse = productMapper.toResponse(productService.findById(productId));
        System.out.println(productResponse.getCategoryId());
        return productResponse;
    }

    @GetMapping("")
    public List<ProductResponse> findAll(@RequestParam(required = false) String label){
        return productService.findAll(label).stream().map(productMapper::toResponse).toList();
    }

    @PutMapping("/{productId}")
    public ProductResponse update(@PathVariable("productId") Long productId, @RequestBody ProductRequest productRequest) {
        Product product = productMapper.toEntity(productRequest);
        return productMapper.toResponse(productService.update(productId, product));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(@PathVariable("productId") Long productId){
        productService.delete(productId);
        return ResponseEntity.noContent().build();
    }



    @PostMapping("/{productId}/variants")
    public ProductVariantResponse createVariant(@PathVariable("productId") Long productId, @RequestBody ProductVariantRequest productVariantRequest) {
        ProductVariant productVariant = productVariantMapper.toEntity(productVariantRequest);
        return productVariantMapper.toResponse(productService.createVariant(productId, productVariant));
    }

    @PutMapping("/{productId}/variants/{variantId}")
    public ProductVariantResponse updateVariant(@PathVariable("productId") Long productId, @PathVariable("variantId") Long variantId, @RequestBody ProductVariantRequest productVariantRequest){
        ProductVariant productVariant = productVariantMapper.toEntity(productVariantRequest);
        return productVariantMapper.toResponse(productService.updateVariant(productId, variantId, productVariant));
    }

    @DeleteMapping("/{productId}/variants/{variantId}")
    public ResponseEntity<Void> deleteVariant(@PathVariable("productId") Long productId, @PathVariable("variantId") Long variantId){
        productService.deleteVariant(productId, variantId);
        return ResponseEntity.noContent().build();
    }



    @PostMapping("/{id}/images")
    public ProductResponse createImage(@PathVariable("id") Long productId, @RequestBody ProductImageRequest productImageRequest) {
        Product product = productService.createImage(productId, productImageMapper.toEntity(productImageRequest));
        return productMapper.toResponse(product);
    }

    @PutMapping("/{productId}/images/{imageId}")
    public ProductImageResponse updateImage(@PathVariable("productId") Long productId, @PathVariable("variantId") Long imageId, @RequestBody ProductImageRequest productImageRequest){
        ProductImage productImage = productImageMapper.toEntity(productImageRequest);
        return productImageMapper.toResponse(productService.updateImage(productId, imageId, productImage));
    }

    @DeleteMapping("/{productId}/images/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable("productId") Long productId, @PathVariable("imageId") Long imageId){
        productService.deleteImage(productId, imageId);
        return ResponseEntity.noContent().build();
    }

}
