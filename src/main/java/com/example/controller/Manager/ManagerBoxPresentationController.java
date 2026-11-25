package com.example.controller.Manager;
import com.example.dto.manager.request.box_presentation.ManagerBoxPresentationRequest;
import com.example.dto.manager.request.pucharse.ManagerPurchaseRequest;
import com.example.dto.manager.response.box_presentation.ManagerBoxPresentationResponse;
import com.example.dto.manager.response.pucharse.ManagerPurchaseResponse;
import com.example.service.manager.box_presentation.ManagerBoxPresentationService;
import com.example.service.manager.pucharse.ManagerPurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/manager/box-presentations")
@RequiredArgsConstructor
public class ManagerBoxPresentationController {

    @Autowired private ManagerBoxPresentationService service;

    @GetMapping
    public Mono<ManagerBoxPresentationResponse> get(@RequestParam Long productId) {
        return service.getByProductId(productId);
    }

    @PutMapping("/{productId}")
    public Mono<ManagerBoxPresentationResponse> save(
            @PathVariable Long productId, @RequestBody ManagerBoxPresentationRequest request) {
        return service.save(productId, request);
    }
}