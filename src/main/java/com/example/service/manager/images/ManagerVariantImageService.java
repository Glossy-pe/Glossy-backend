package com.example.service.manager.images;

import com.example.dto.manager.request.images.ManagerVariantImageRequest;
import com.example.dto.manager.response.images.ManagerVariantImageResponse;
import com.example.entity.ProductVariantImage;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.manager.ManagerVariantImageMapper;
import com.example.repository.VariantImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private CloudinaryService cloudinaryService;

    public Flux<ManagerVariantImageResponse> getByVariantId(Long id){
        return variantImageRepository.findByProductVariantId(id).map(managerVariantImageMapper::toResponse);
    }

    public Mono<ManagerVariantImageResponse> update(Long id, ManagerVariantImageRequest request){
        return variantImageRepository.findById(id)
                .flatMap(variantImage -> {

                    managerVariantImageMapper.updateEntity(request, variantImage);

                    return variantImageRepository.save(variantImage);
                })
                .map(managerVariantImageMapper::toResponse);
    }

    public Mono<ManagerVariantImageResponse> create(ManagerVariantImageRequest request){
        ProductVariantImage entity = managerVariantImageMapper.toEntity(request);
        return variantImageRepository.save(entity).map(managerVariantImageMapper::toResponse);
    }

    public Mono<Void> delete(Long id) {
        return variantImageRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Image not found with id: " + id)))
                .flatMap(image ->
                        cloudinaryService.delete(image.getUrl(), image.getResourceType())
                                .then(variantImageRepository.deleteById(image.getId()))
                );
    }
}
