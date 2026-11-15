package com.example.service.payment;

import com.example.dto.manager.response.payment.ManagerPaymentProofResponse;
import com.example.entity.OrderPaymentProof;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.OrderPaymentProofRepository;
import com.example.repository.OrderRepository;
import com.example.service.manager.images.CloudflareStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class ManagerPaymentProofService {

    @Autowired
    private OrderPaymentProofRepository proofRepository;
    @Autowired private OrderRepository orderRepository;   // ajusta al nombre real
    @Autowired private CloudflareStorageService cloudflareStorageService;

    public Flux<ManagerPaymentProofResponse> getByOrder(Long orderId) {
        return proofRepository.findByOrderIdOrderByCreatedAtDesc(orderId).map(this::toResponse);
    }

    public Flux<ManagerPaymentProofResponse> create(Long orderId, Flux<FilePart> files) {
        return orderRepository.existsById(orderId)
                .flatMapMany(exists -> exists
                        ? files
                        : Flux.<FilePart>error(new ResourceNotFoundException("Orden no encontrada: " + orderId)))
                .filter(f -> f.headers().getContentType() != null
                        && f.headers().getContentType().toString().startsWith("image/"))
                .concatMap(file -> cloudflareStorageService.upload(file, "payment-proofs/" + orderId))
                .concatMap(url -> {
                    OrderPaymentProof p = new OrderPaymentProof();
                    p.setOrderId(orderId);
                    p.setUrl(url);
                    p.setCreatedAt(LocalDateTime.now());
                    return proofRepository.save(p);
                })
                .map(this::toResponse);
    }

    public Mono<Void> delete(Long orderId, Long proofId) {
        return proofRepository.findById(proofId)
                .filter(p -> p.getOrderId().equals(orderId))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Comprobante no encontrado: " + proofId)))
                .flatMap(p -> cloudflareStorageService.delete(p.getUrl())
                        .then(proofRepository.deleteById(p.getId())));
    }

    private ManagerPaymentProofResponse toResponse(OrderPaymentProof p) {
        return new ManagerPaymentProofResponse(p.getId(), p.getUrl(), p.getNote(), p.getCreatedAt());
    }
}