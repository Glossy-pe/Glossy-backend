package com.example.service.manager.images;

import com.example.dto.manager.request.images.ManagerProductImageRequest;
import com.example.dto.manager.response.images.ManagerProductImageResponse;
import com.example.entity.ProductImage;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.manager.ManagerProductImageMapper;
import com.example.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ManagerProductImageService {

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ManagerProductImageMapper managerProductImageMapper;

    @Autowired
    private CloudflareStorageService cloudflareStorageService;

    public Flux<ManagerProductImageResponse> getByProductId(Long id){
        return productImageRepository.findByProductId(id).map(managerProductImageMapper::toResponse);
    }

    public Mono<ManagerProductImageResponse> create(FilePart file, ManagerProductImageRequest request){
        boolean isVideo = file.headers().getContentType() != null
                && file.headers().getContentType().toString().startsWith("video/");
        request.setResourceType(isVideo ? "video" : "image");

        String folder = "products/" + request.getProductId();

        return cloudflareStorageService.upload(file, folder)
                .flatMap(url -> {
                    request.setUrl(url);
                    ProductImage entity = managerProductImageMapper.toEntity(request);
                    return productImageRepository.save(entity);
                })
                .map(managerProductImageMapper::toResponse);
    }

    // Update solo de metadata (position, mainImage) — sin tocar el archivo
    public Mono<ManagerProductImageResponse> update(Long id, ManagerProductImageRequest request){
        return productImageRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Image not found with id: " + id)))
                .flatMap(productImage -> {
                    managerProductImageMapper.updateEntity(request, productImage);
                    return productImageRepository.save(productImage);
                })
                .map(managerProductImageMapper::toResponse);
    }

    // Reemplazar el archivo de una imagen existente
    public Mono<ManagerProductImageResponse> replaceImage(Long id, FilePart file){
        return productImageRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Image not found with id: " + id)))
                .flatMap(productImage -> {
                    String oldUrl = productImage.getUrl();
                    String folder = "products/" + productImage.getProductId();
                    boolean isVideo = file.headers().getContentType() != null
                            && file.headers().getContentType().toString().startsWith("video/");

                    return cloudflareStorageService.upload(file, folder)
                            .flatMap(newUrl -> {
                                productImage.setUrl(newUrl);
                                productImage.setResourceType(isVideo ? "video" : "image");
                                return cloudflareStorageService.delete(oldUrl)
                                        .then(productImageRepository.save(productImage));
                            });
                })
                .map(managerProductImageMapper::toResponse);
    }

    public Mono<Void> delete(Long id) {
        return productImageRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Image not found with id: " + id)))
                .flatMap(image ->
                        cloudflareStorageService.delete(image.getUrl())
                                .then(productImageRepository.deleteById(image.getId()))
                );
    }
}
