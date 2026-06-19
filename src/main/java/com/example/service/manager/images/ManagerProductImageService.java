package com.example.service.manager.images;

import com.example.dto.manager.request.images.ManagerProductImageRequest;
import com.example.dto.manager.response.images.ManagerProductImageResponse;
import com.example.entity.ProductImage;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.manager.ManagerProductImageMapper;
import com.example.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private CloudinaryService cloudinaryService;

    public Flux<ManagerProductImageResponse> getByProductId(Long id){
        return productImageRepository.findByProductId(id).map(managerProductImageMapper::toResponse);
    }

    public Mono<ManagerProductImageResponse> update(Long id, ManagerProductImageRequest request){
        return productImageRepository.findById(id)
                .flatMap(productImage -> {

                    managerProductImageMapper.updateEntity(request, productImage);

                    return productImageRepository.save(productImage);
                })
                .map(managerProductImageMapper::toResponse);
    }

    public Mono<ManagerProductImageResponse> create(ManagerProductImageRequest request){
        ProductImage entity = managerProductImageMapper.toEntity(request);
        return productImageRepository.save(entity).map(managerProductImageMapper::toResponse);
    }

    public Mono<Void> delete(Long id) {
        return productImageRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Image not found with id: " + id)))
                .flatMap(image ->
                        cloudinaryService.delete(image.getUrl(), image.getResourceType())
                                .then(productImageRepository.deleteById(image.getId()))
                );
    }
}
