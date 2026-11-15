package com.example.controller.Manager;

import com.example.dto.manager.response.payment.ManagerPaymentProofResponse;
import com.example.service.payment.ManagerPaymentProofService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/manager/orders/{orderId}/payment-proofs")
public class ManagerPaymentProofController {

    @Autowired
    private ManagerPaymentProofService service;

    @GetMapping
    public Flux<ManagerPaymentProofResponse> getAll(@PathVariable Long orderId) {
        return service.getByOrder(orderId);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Flux<ManagerPaymentProofResponse> upload(@PathVariable Long orderId,
                                                    @RequestPart("files") Flux<FilePart> files) {
        return service.create(orderId, files);
    }

    @DeleteMapping("/{proofId}")
    public Mono<Void> delete(@PathVariable Long orderId, @PathVariable Long proofId) {
        return service.delete(orderId, proofId);
    }
}