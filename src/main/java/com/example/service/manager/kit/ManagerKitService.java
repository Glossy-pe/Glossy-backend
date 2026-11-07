package com.example.service.manager.kit;

import com.example.dto.manager.request.kit.ManagerKitItemRequest;
import com.example.dto.manager.request.kit.ManagerKitRequest;
import com.example.dto.manager.response.kit.ManagerKitDetailResponse;
import com.example.dto.manager.response.kit.ManagerKitItemResponse;
import com.example.dto.manager.response.kit.ManagerKitResponse;
import com.example.entity.Kit;
import com.example.entity.KitItem;
import com.example.entity.Product;
import com.example.mapper.manager.ManagerKitMapper;
import com.example.repository.KitItemRepository;
import com.example.repository.KitRepository;
import com.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ManagerKitService {

    @Autowired
    private KitRepository kitRepository;

    @Autowired
    private KitItemRepository kitItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ManagerKitMapper kitMapper;

    // ── Kits ───────────────────────────────────────────

    public Flux<ManagerKitResponse> getAll() {
        return kitRepository.findAll().map(kitMapper::toResponse);
    }

    public Mono<ManagerKitDetailResponse> getById(Long kitId) {
        return kitRepository.findById(kitId)
                .switchIfEmpty(fail(HttpStatus.NOT_FOUND, "Kit no encontrado"))
                .flatMap(kit ->
                        getItemsWithProductName(kitId)
                                .map(items -> {
                                    ManagerKitDetailResponse response = kitMapper.toDetailResponse(kit);
                                    response.setItems(items);
                                    return response;
                                })
                );
    }

    public Mono<ManagerKitResponse> create(ManagerKitRequest request) {
        Kit entity = kitMapper.toEntity(request);
        entity.setCreatedAt(LocalDateTime.now());

        return kitRepository.save(entity)
                .map(kitMapper::toResponse);
    }

    public Mono<ManagerKitResponse> update(Long kitId, ManagerKitRequest request) {
        return kitRepository.findById(kitId)
                .switchIfEmpty(fail(HttpStatus.NOT_FOUND, "Kit no encontrado"))
                .flatMap(kit -> {
                    kitMapper.updateEntity(request, kit);
                    return kitRepository.save(kit);
                })
                .map(kitMapper::toResponse);
    }

    // Los items se borran solos por ON DELETE CASCADE
    public Mono<Void> delete(Long kitId) {
        return kitRepository.deleteById(kitId);
    }

    // ── Items ──────────────────────────────────────────

    public Mono<ManagerKitItemResponse> addItem(Long kitId, ManagerKitItemRequest request) {
        if (request.getProductId() == null) {
            return fail(HttpStatus.BAD_REQUEST, "productId es obligatorio");
        }
        if (request.getPosition() == null || request.getPosition() < 1) {
            return fail(HttpStatus.BAD_REQUEST, "La posición debe ser mayor o igual a 1");
        }

        Long productId = request.getProductId();
        Integer position = request.getPosition();

        return kitRepository.existsById(kitId)
                .flatMap(kitExists -> kitExists
                        ? productRepository.existsById(productId)
                        : this.<Boolean>fail(HttpStatus.NOT_FOUND, "Kit no encontrado"))
                .flatMap(productExists -> productExists
                        ? kitItemRepository.existsByKitIdAndPosition(kitId, position)
                        : this.<Boolean>fail(HttpStatus.NOT_FOUND, "Producto no encontrado"))
                .flatMap(positionTaken -> positionTaken
                        ? this.<Boolean>fail(HttpStatus.CONFLICT, "Esa posición ya está ocupada en el kit")
                        : kitItemRepository.existsByKitIdAndProductId(kitId, productId))
                .flatMap(productInKit -> {
                    if (productInKit) {
                        return this.<KitItem>fail(HttpStatus.CONFLICT, "El producto ya está en el kit");
                    }
                    KitItem item = new KitItem();
                    item.setKitId(kitId);
                    item.setProductId(productId);
                    item.setPosition(position);
                    return kitItemRepository.save(item);
                })
                .map(kitMapper::toItemResponse);
    }

    public Mono<ManagerKitItemResponse> updateItem(Long kitId, Long itemId, ManagerKitItemRequest request) {
        Long newProductId = request.getProductId();
        Integer newPosition = request.getPosition();

        if (newProductId == null && newPosition == null) {
            return fail(HttpStatus.BAD_REQUEST, "Envía productId, position o ambos");
        }
        if (newPosition != null && newPosition < 1) {
            return fail(HttpStatus.BAD_REQUEST, "La posición debe ser mayor o igual a 1");
        }

        return findItemOfKit(kitId, itemId)
                .flatMap(item ->
                        checkPositionFree(kitId, item, newPosition)
                                .then(checkProductAllowed(kitId, item, newProductId))
                                .thenReturn(item))
                .flatMap(item -> {
                    if (newPosition != null) item.setPosition(newPosition);
                    if (newProductId != null) item.setProductId(newProductId);
                    return kitItemRepository.save(item);
                })
                .map(kitMapper::toItemResponse);
    }

    private Mono<Void> checkPositionFree(Long kitId, KitItem item, Integer newPosition) {
        if (newPosition == null || newPosition.equals(item.getPosition())) {
            return Mono.empty();
        }
        return kitItemRepository.existsByKitIdAndPosition(kitId, newPosition)
                .flatMap(taken -> taken
                        ? this.<Void>fail(HttpStatus.CONFLICT, "Esa posición ya está ocupada en el kit")
                        : Mono.<Void>empty());
    }

    private Mono<Void> checkProductAllowed(Long kitId, KitItem item, Long newProductId) {
        if (newProductId == null || newProductId.equals(item.getProductId())) {
            return Mono.empty();
        }
        return productRepository.existsById(newProductId)
                .flatMap(exists -> !exists
                        ? this.<Void>fail(HttpStatus.NOT_FOUND, "Producto no encontrado")
                        : kitItemRepository.existsByKitIdAndProductId(kitId, newProductId)
                        .flatMap(inKit -> inKit
                                ? this.<Void>fail(HttpStatus.CONFLICT, "El producto ya está en el kit")
                                : Mono.<Void>empty()));
    }

    public Mono<Void> removeItem(Long kitId, Long itemId) {
        return findItemOfKit(kitId, itemId)
                .flatMap(kitItemRepository::delete);
    }

    // ── Helpers ────────────────────────────────────────

    private Mono<KitItem> findItemOfKit(Long kitId, Long itemId) {
        return kitItemRepository.findById(itemId)
                .filter(item -> item.getKitId().equals(kitId))
                .switchIfEmpty(fail(HttpStatus.NOT_FOUND, "Item no encontrado en este kit"));
    }

    private Mono<List<ManagerKitItemResponse>> getItemsWithProductName(Long kitId) {
        return kitItemRepository.findByKitIdOrderByPosition(kitId)
                .collectList()
                .flatMap(items -> {
                    if (items.isEmpty()) {
                        return Mono.just(List.<ManagerKitItemResponse>of());
                    }
                    List<Long> productIds = items.stream().map(KitItem::getProductId).toList();

                    return productRepository.findAllByIdIn(productIds)
                            .collectMap(Product::getId, Product::getName)
                            .map(names -> items.stream()
                                    .map(item -> {
                                        ManagerKitItemResponse response = kitMapper.toItemResponse(item);
                                        response.setProductName(names.get(item.getProductId()));
                                        return response;
                                    })
                                    .toList());
                });
    }

    private <T> Mono<T> fail(HttpStatus status, String message) {
        return Mono.error(new ResponseStatusException(status, message));
    }
}