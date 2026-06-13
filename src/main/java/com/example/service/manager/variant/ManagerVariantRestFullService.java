package com.example.service.manager.variant;

import com.example.dto.manager.response.variant.ManagerVariantQueryProjectionResponse;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.VariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ManagerVariantRestFullService {

    @Autowired
    private VariantRepository variantRepository;

    public Mono<ManagerVariantQueryProjectionResponse> getVariantDetail(Long variantId) {
        return variantRepository.findVariantDetailById(variantId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Variant not found with id: " + variantId)));
    }
}
