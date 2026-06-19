package com.example.controller.Manager;

import com.example.dto.manager.request.images.ManagerProductImageRequest;
import com.example.dto.manager.request.images.ManagerVariantImageRequest;
import com.example.dto.manager.response.images.ManagerProductImageResponse;
import com.example.dto.manager.response.images.ManagerVariantImageResponse;
import com.example.service.manager.images.ManagerVariantImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/manager/variant-images")
@RequiredArgsConstructor
public class ManagerVariantImageController {

    @Autowired
    private ManagerVariantImageService managerVariantImageService;

    @PostMapping
    public Mono<ManagerVariantImageResponse> create(@RequestBody ManagerVariantImageRequest request) {
        return managerVariantImageService.create(request);
    }

    @PutMapping("/{id}")
    public Mono<ManagerVariantImageResponse> update(
            @PathVariable Long id,
            @RequestBody ManagerVariantImageRequest request
    ) {
        return managerVariantImageService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return managerVariantImageService.delete(id);
    }

}
