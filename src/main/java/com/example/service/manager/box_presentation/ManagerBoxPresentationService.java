package com.example.service.manager.box_presentation;
import com.example.dto.manager.request.box_presentation.ManagerBoxPresentationRequest;
import com.example.dto.manager.response.box_presentation.ManagerBoxPresentationResponse;
import com.example.entity.BoxPresentation;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
public class ManagerBoxPresentationService {

    @Autowired private BoxPresentationRepository boxPresentationRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private VariantRepository variantRepository;

    public Mono<ManagerBoxPresentationResponse> getByProductId(Long productId) {
        return boxPresentationRepository.findByProductId(productId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(
                        "El producto " + productId + " no tiene Presentación BOX")))
                .flatMap(this::toResponse);
    }

    @Transactional
    public Mono<ManagerBoxPresentationResponse> save(Long productId, ManagerBoxPresentationRequest r) {
        return Mono.defer(() -> {
                    if (r.getTotalUnits() == null || r.getTotalUnits() < 1)
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "totalUnits debe ser mayor a 0");
                    return productRepository.existsById(productId);
                })
                .flatMap(exists -> exists
                        ? boxPresentationRepository.findByProductId(productId)
                        .defaultIfEmpty(newPresentation(productId))
                        : Mono.<BoxPresentation>error(new ResourceNotFoundException(
                        "Product not found with id: " + productId)))
                .flatMap(p -> {
                    p.setTotalUnits(r.getTotalUnits());
                    return boxPresentationRepository.save(p);
                })
                .flatMap(this::toResponse);
    }

    private BoxPresentation newPresentation(Long productId) {
        BoxPresentation p = new BoxPresentation();
        p.setProductId(productId);
        return p;
    }

    private Mono<ManagerBoxPresentationResponse> toResponse(BoxPresentation p) {
        return variantRepository.findByProductId(p.getProductId())
                .filter(v -> Boolean.TRUE.equals(v.getActive()) && !Boolean.TRUE.equals(v.getDeleted()))
                .count()
                .map(count -> {
                    int n = count.intValue();
                    boolean valid = n > 0 && p.getTotalUnits() % n == 0;
                    ManagerBoxPresentationResponse res = new ManagerBoxPresentationResponse();
                    res.setProductId(p.getProductId());
                    res.setTotalUnits(p.getTotalUnits());
                    res.setActiveVariants(n);
                    res.setUnitsPerVariant(valid ? p.getTotalUnits() / n : null);
                    res.setValid(valid);
                    return res;
                });
    }
}