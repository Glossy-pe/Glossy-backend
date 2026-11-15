package com.example.service.manager.kit;

import com.example.dto.manager.request.kit.ManagerKitItemRequest;
import com.example.dto.manager.request.kit.ManagerKitRequest;
import com.example.dto.manager.response.kit.*;
import com.example.entity.*;
import com.example.mapper.manager.ManagerKitMapper;
import com.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ManagerKitService {

    @Autowired
    private KitRepository kitRepository;

    @Autowired
    private KitItemRepository kitItemRepository;

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ManagerKitMapper kitMapper;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private VariantImageRepository variantImageRepository;

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

    public Mono<List<ManagerKitItemWithVariantsResponse>> getItemsWithVariants(Long kitId) {
        return kitRepository.existsById(kitId)
                .flatMap(exists -> exists
                        ? kitItemRepository.findByKitIdOrderByPosition(kitId).collectList()
                        : this.<List<KitItem>>fail(HttpStatus.NOT_FOUND, "Kit no encontrado"))
                .flatMap(items -> {
                    if (items.isEmpty()) {
                        return Mono.just(List.<ManagerKitItemWithVariantsResponse>of());
                    }
                    List<Long> productIds = items.stream().map(KitItem::getProductId).toList();

                    return Mono.zip(
                            productRepository.findAllByIdIn(productIds)
                                    .collectMap(Product::getId, Product::getName),
                            productImageRepository.findAllByProductIdIn(productIds).collectList(),
                            variantRepository.findAllByProductIdInAndActiveTrue(productIds).collectList()
                    ).flatMap(t -> {
                        Map<Long, String> names = t.getT1();
                        List<ProductVariant> variants = t.getT3();
                        List<Long> variantIds = variants.stream().map(ProductVariant::getId).toList();

                        Map<Long, String> productImg = t.getT2().stream()
                                .filter(i -> !"video".equalsIgnoreCase(i.getResourceType()))
                                .sorted(Comparator
                                        .comparing((ProductImage i) -> !Boolean.TRUE.equals(i.getMainImage()))
                                        .thenComparingInt(ProductImage::getPosition))
                                .collect(Collectors.toMap(ProductImage::getProductId,
                                        ProductImage::getUrl, (a, b) -> a));

                        Mono<List<ProductVariantImage>> variantImages = variantIds.isEmpty()
                                ? Mono.just(List.of())
                                : variantImageRepository.findAllByProductVariantIdIn(variantIds).collectList();

                        return variantImages.map(vi -> {
                            Map<Long, String> variantImg = vi.stream()
                                    .filter(i -> !"video".equalsIgnoreCase(i.getResourceType()))
                                    .sorted(Comparator
                                            .comparing((ProductVariantImage i) -> !Boolean.TRUE.equals(i.getMainImage()))
                                            .thenComparingInt(ProductVariantImage::getPosition))
                                    .collect(Collectors.toMap(ProductVariantImage::getProductVariantId,
                                            ProductVariantImage::getUrl, (a, b) -> a));

                            Map<Long, List<ProductVariant>> byProduct = variants.stream()
                                    .collect(Collectors.groupingBy(ProductVariant::getProductId));

                            return items.stream().map(item -> {
                                var r = new ManagerKitItemWithVariantsResponse();
                                r.setItemId(item.getId());
                                r.setPosition(item.getPosition());
                                r.setProductId(item.getProductId());
                                r.setProductName(names.get(item.getProductId()));
                                r.setImage(productImg.get(item.getProductId()));
                                r.setVariants(byProduct.getOrDefault(item.getProductId(), List.of())
                                        .stream()
                                        .sorted(Comparator.comparing(ProductVariant::getPosition,
                                                Comparator.nullsLast(Comparator.naturalOrder())))
                                        .map(v -> toVariantResponse(v, variantImg.get(v.getId())))
                                        .toList());
                                return r;
                            }).toList();
                        });
                    });
                });
    }

    private ManagerKitVariantResponse toVariantResponse(ProductVariant v, String image) {
        var r = new ManagerKitVariantResponse();
        r.setId(v.getId());
        r.setToneName(v.getToneName());
        r.setToneCode(v.getToneCode());
        r.setPrice(v.getPrice());
        r.setStock(v.getStock());
        r.setPosition(v.getPosition());
        r.setImage(image);
        return r;
    }
}