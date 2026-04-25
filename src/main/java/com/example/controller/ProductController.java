package com.example.controller;

import com.example.dtos.request.product.ProductRequest;
import com.example.dtos.response.product.ProductResponse;
import com.example.dtos.response.full.ProductResponseFull;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequestMapping("products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("")
    public ProductResponse create(@RequestBody ProductRequest productRequestV2){
        return productService.create(productRequestV2);
    }

    @GetMapping("")
    public List<ProductResponse> findAll(
            @RequestParam(required = false) Long labelId,
            @RequestParam(required = false) Long categoryId
    ) {
        return productService.findAll();
    }

    @GetMapping("/{productId}")
    public ProductResponse findById(@PathVariable("productId") Long productId){
        return productService.findById(productId);
    }

    @PutMapping("/{productId}")
    public ProductResponse update(@PathVariable("productId") Long productId, @RequestBody ProductRequest productRequest){
        return productService.update(productId ,productRequest);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(@PathVariable("productId") Long productId){
        productService.delete(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/full")
    public Page<ProductResponseFull> findAllFull(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) Long categoryId,
        @RequestParam(required = false) Long labelId   // 👈 agrega
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.findAllFull(pageable, categoryId, labelId);
    }

    @GetMapping("/full/search")
    public Page<ProductResponseFull> search(
            @RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long labelId
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.searchFull(q, pageable, categoryId, labelId);
    }

    @GetMapping("/full/{id}")
    public ProductResponseFull findByIdFull(@PathVariable Long id) {
        return productService.findByIdFull(id);
    }


    

    // Endpoint para re-indexar todos los productos existentes (usar una vez)
    @PostMapping("/reindex")
    public ResponseEntity<String> reindex() {
        productService.reindexAll();
        return ResponseEntity.ok("Re-indexación iniciada");
    }

    @PutMapping("/{id}/labels")
    public ResponseEntity<Void> updateLabels(
        @PathVariable Long id,
        @RequestBody List<Long> labelIds
    ) {
        productService.updateProductLabels(id, labelIds);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/migrate-slugs")
public ResponseEntity<String> migrateSlugs() {
    productService.migrateslugs();
    return ResponseEntity.ok("Slugs migrados correctamente");
}
}
