package com.example.service.guest.variant;

import com.example.dto.guest.response.variant.GuestVariantResponseFull;
import com.example.mapper.guest.GuestVariantImageMapper;
import com.example.mapper.guest.GuestVariantMapper;
import com.example.repository.VariantImageRepository;
import com.example.repository.VariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GuestVariantRestFullService {
    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private VariantImageRepository variantImageRepository;

    @Autowired
    private GuestVariantImageMapper variantImageMapper;

    @Autowired
    private GuestVariantMapper variantMapper;

    public Mono<GuestVariantResponseFull> getById(Long variantId) {
        return Mono.zip(
                variantRepository.findById(variantId),
                variantImageRepository.findByProductVariantId(variantId).collectList()
        ).map(tuple -> {
            GuestVariantResponseFull response = variantMapper.toResponseFull(tuple.getT1());
            response.setImages(tuple.getT2().stream().map(variantImageMapper::toResponse).toList());
            return response;
        });
    }
}
