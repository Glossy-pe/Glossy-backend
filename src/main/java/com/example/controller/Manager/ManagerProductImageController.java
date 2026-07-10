package com.example.controller.Manager;

import com.example.dto.manager.request.images.ManagerProductImageRequest;
import com.example.dto.manager.request.variant.ManagerVariantRequest;
import com.example.dto.manager.response.images.ManagerProductImageResponse;
import com.example.dto.manager.response.variant.ManagerVariantResponse;
import com.example.service.manager.images.ManagerProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/manager/product-images")
public class ManagerProductImageController {

    @Autowired
    private ManagerProductImageService managerProductImageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ManagerProductImageResponse> create(
            @RequestPart("file") FilePart file,
            @RequestPart("position") String position,
            @RequestPart("mainImage") String mainImage,
            @RequestPart("productId") String productId
    ) {
        ManagerProductImageRequest request = new ManagerProductImageRequest();
        request.setPosition(Integer.parseInt(position));
        request.setMainImage(Boolean.parseBoolean(mainImage));
        request.setProductId(Long.parseLong(productId));
        return managerProductImageService.create(file, request);
    }

    @PutMapping("/{id}")
    public Mono<ManagerProductImageResponse> update(
            @PathVariable Long id,
            @RequestBody ManagerProductImageRequest request
    ) {
        return managerProductImageService.update(id, request);
    }

    @PutMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ManagerProductImageResponse> replaceImage(
            @PathVariable Long id,
            @RequestPart("file") FilePart file
    ) {
        return managerProductImageService.replaceImage(id, file);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return managerProductImageService.delete(id);
    }
}