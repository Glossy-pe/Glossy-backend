package com.example.controller.Manager;

import com.example.dto.manager.request.kit.ManagerKitItemRequest;
import com.example.dto.manager.request.kit.ManagerKitRequest;
import com.example.dto.manager.response.kit.ManagerKitDetailResponse;
import com.example.dto.manager.response.kit.ManagerKitItemResponse;
import com.example.dto.manager.response.kit.ManagerKitResponse;
import com.example.service.manager.kit.ManagerKitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/manager/kits")
public class ManagerKitController {

    @Autowired
    private ManagerKitService managerKitService;

    @GetMapping
    public Flux<ManagerKitResponse> getAll() {
        return managerKitService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<ManagerKitDetailResponse> getById(@PathVariable Long id) {
        return managerKitService.getById(id);
    }

    @PostMapping
    public Mono<ManagerKitResponse> create(@RequestBody ManagerKitRequest request) {
        return managerKitService.create(request);
    }

    @PutMapping("/{id}")
    public Mono<ManagerKitResponse> update(@PathVariable Long id,
                                           @RequestBody ManagerKitRequest request) {
        return managerKitService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return managerKitService.delete(id);
    }

    // ── Items ──

    @PostMapping("/{kitId}/items")
    public Mono<ManagerKitItemResponse> addItem(@PathVariable Long kitId,
                                                @RequestBody ManagerKitItemRequest request) {
        return managerKitService.addItem(kitId, request);
    }

    @PutMapping("/{kitId}/items/{itemId}")
    public Mono<ManagerKitItemResponse> updateItem(@PathVariable Long kitId,
                                                   @PathVariable Long itemId,
                                                   @RequestBody ManagerKitItemRequest request) {
        return managerKitService.updateItem(kitId, itemId, request);
    }

    @DeleteMapping("/{kitId}/items/{itemId}")
    public Mono<Void> removeItem(@PathVariable Long kitId, @PathVariable Long itemId) {
        return managerKitService.removeItem(kitId, itemId);
    }
}