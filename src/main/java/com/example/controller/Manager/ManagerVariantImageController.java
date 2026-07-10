package com.example.controller.Manager;

import com.example.dto.manager.request.images.ManagerProductImageRequest;
import com.example.dto.manager.request.images.ManagerVariantImageRequest;
import com.example.dto.manager.response.images.ManagerProductImageResponse;
import com.example.dto.manager.response.images.ManagerVariantImageResponse;
import com.example.service.manager.images.ManagerVariantImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/manager/variant-images")
public class ManagerVariantImageController {

    @Autowired
    private ManagerVariantImageService managerVariantImageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ManagerVariantImageResponse> create(
            @RequestPart("file") FilePart file,
            @RequestPart("position") String position,
            @RequestPart("mainImage") String mainImage,
            @RequestPart("productVariantId") String productVariantId
    ) {
        ManagerVariantImageRequest request = new ManagerVariantImageRequest();
        request.setPosition(Integer.parseInt(position));
        request.setMainImage(Boolean.parseBoolean(mainImage));
        request.setProductVariantId(Long.parseLong(productVariantId));
        return managerVariantImageService.create(file, request);
    }

    @PutMapping("/{id}")
    public Mono<ManagerVariantImageResponse> update(
            @PathVariable Long id,
            @RequestBody ManagerVariantImageRequest request
    ) {
        return managerVariantImageService.update(id, request);
    }

    @PutMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ManagerVariantImageResponse> replaceImage(
            @PathVariable Long id,
            @RequestPart("file") FilePart file
    ) {
        return managerVariantImageService.replaceImage(id, file);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return managerVariantImageService.delete(id);
    }
}
