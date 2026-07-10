package com.example.service.manager.images;

import com.example.dto.manager.request.images.ManagerVariantImageRequest;
import com.example.dto.manager.response.images.ManagerVariantImageResponse;
import com.example.entity.ProductVariantImage;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.manager.ManagerVariantImageMapper;
import com.example.repository.VariantImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ManagerVariantImageService {

    @Autowired
    private VariantImageRepository variantImageRepository;

    @Autowired
    private ManagerVariantImageMapper managerVariantImageMapper;

    @Autowired
    private CloudflareStorageService cloudflareStorageService;

    public Flux<ManagerVariantImageResponse> getByVariantId(Long id){
        return variantImageRepository.findByProductVariantId(id).map(managerVariantImageMapper::toResponse);
    }

    public Mono<ManagerVariantImageResponse> create(FilePart file, ManagerVariantImageRequest request){
        boolean isVideo = file.headers().getContentType() != null
                && file.headers().getContentType().toString().startsWith("video/");
        request.setResourceType(isVideo ? "video" : "image");

        String folder = "variants/" + request.getProductVariantId();

        return cloudflareStorageService.upload(file, folder)
                .flatMap(url -> {
                    request.setUrl(url);
                    ProductVariantImage entity = managerVariantImageMapper.toEntity(request);
                    return variantImageRepository.save(entity);
                })
                .map(managerVariantImageMapper::toResponse);
    }

    public Mono<ManagerVariantImageResponse> update(Long id, ManagerVariantImageRequest request){
        return variantImageRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Image not found with id: " + id)))
                .flatMap(variantImage -> {
                    managerVariantImageMapper.updateEntity(request, variantImage);
                    return variantImageRepository.save(variantImage);
                })
                .map(managerVariantImageMapper::toResponse);
    }

    public Mono<ManagerVariantImageResponse> replaceImage(Long id, FilePart file){
        return variantImageRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Image not found with id: " + id)))
                .flatMap(variantImage -> {
                    String oldUrl = variantImage.getUrl();
                    String folder = "variants/" + variantImage.getProductVariantId();
                    boolean isVideo = file.headers().getContentType() != null
                            && file.headers().getContentType().toString().startsWith("video/");

                    return cloudflareStorageService.upload(file, folder)
                            .flatMap(newUrl -> {
                                variantImage.setUrl(newUrl);
                                variantImage.setResourceType(isVideo ? "video" : "image");
                                return cloudflareStorageService.delete(oldUrl)
                                        .then(variantImageRepository.save(variantImage));
                            });
                })
                .map(managerVariantImageMapper::toResponse);
    }

    public Mono<Void> delete(Long id) {
        return variantImageRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Image not found with id: " + id)))
                .flatMap(image ->
                        cloudflareStorageService.delete(image.getUrl())
                                .then(variantImageRepository.deleteById(image.getId()))
                );
    }
}