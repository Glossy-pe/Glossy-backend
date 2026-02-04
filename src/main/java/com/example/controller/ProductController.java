package com.example.controller;

import com.example.dtos.request.ProductImageRequest;
import com.example.dtos.request.ProductRequest;
import com.example.dtos.request.ProductVariantRequest;
import com.example.dtos.response.ProductResponse;
import com.example.entity.Product;
import com.example.mapper.ProductImageMapper;
import com.example.mapper.ProductMapper;
import com.example.mapper.ProductVariantMapper;
import com.example.service.CategoryService;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/{id}")
    public ProductResponse findById(@PathVariable("id") Long id){
        ProductResponse productResponse = productMapper.toResponse(productService.findById(id));
        System.out.println(productResponse.getCategoryId());
        return productResponse;
    }

    @PostMapping
    public ProductResponse save(@RequestBody ProductRequest productRequest) {
        Product product = productMapper.toEntity(productRequest);
        return productMapper.toResponse(productService.save(product));
    }

    @PostMapping("/{id}/variants")
    public ProductResponse addVariantToProduct(@PathVariable("id") Long productId, @RequestBody ProductVariantRequest productVariantRequest) {
        Product product = productService.addVariantToProduct(productId, productVariantMapper.toEntity(productVariantRequest));
        return productMapper.toResponse(product);
    }

    @PostMapping("/{id}/images")
    public ProductResponse addImageToProduct(@PathVariable("id") Long productId, @RequestBody ProductImageRequest productImageRequest) {
        Product product = productService.addImageToProduct(productId, productImageMapper.toEntity(productImageRequest));
        return productMapper.toResponse(product);
    }

    @GetMapping("")
    public List<ProductResponse> findAll(@RequestParam(required = false) String label){
        return productService.findAll(label).stream().map(productMapper::toResponse).toList();
    }



}
