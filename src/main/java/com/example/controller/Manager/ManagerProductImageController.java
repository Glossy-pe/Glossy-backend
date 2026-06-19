package com.example.controller.Manager;

import com.example.dto.manager.request.images.ManagerProductImageRequest;
import com.example.dto.manager.request.variant.ManagerVariantRequest;
import com.example.dto.manager.response.images.ManagerProductImageResponse;
import com.example.dto.manager.response.variant.ManagerVariantResponse;
import com.example.service.manager.images.ManagerProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/manager/product-images")
@RequiredArgsConstructor
public class ManagerProductImageController {

    @Autowired
    private ManagerProductImageService managerProductImageService;

    @PostMapping
    public Mono<ManagerProductImageResponse> create(@RequestBody ManagerProductImageRequest request) {
        return managerProductImageService.create(request);
    }

    @PutMapping("/{id}")
    public Mono<ManagerProductImageResponse> update(
            @PathVariable Long id,
            @RequestBody ManagerProductImageRequest request
    ) {
        return managerProductImageService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return managerProductImageService.delete(id);
    }

}
