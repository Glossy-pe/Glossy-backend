package com.example.service.manager.pucharse;

import com.example.dto.manager.request.pucharse.ManagerPurchaseRequest;
import com.example.dto.manager.response.pucharse.ManagerPurchaseResponse;
import com.example.entity.ProductVariant;
import com.example.entity.Purchase;
import com.example.entity.PurchaseItem;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.ProductRepository;
import com.example.repository.PurchaseItemRepository;
import com.example.repository.PurchaseRepository;
import com.example.repository.VariantRepository;
import com.example.utils.PurchaseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ManagerPurchaseService {

    @Autowired private PurchaseRepository purchaseRepository;
    @Autowired private PurchaseItemRepository purchaseItemRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private VariantRepository variantRepository;

    @Transactional
    public Mono<ManagerPurchaseResponse> create(ManagerPurchaseRequest r) {
        return Mono.defer(() -> {
                    validateBasic(r);
                    return productRepository.existsById(r.getProductId());
                })
                .flatMap(exists -> exists
                        ? variantRepository.findByProductId(r.getProductId())
                        .filter(v -> Boolean.TRUE.equals(v.getActive())
                                && !Boolean.TRUE.equals(v.getDeleted()))
                        .map(ProductVariant::getId)
                        .collect(Collectors.toSet())
                        : Mono.<Set<Long>>error(new ResourceNotFoundException(
                        "Product not found with id: " + r.getProductId())))
                .flatMap(validVariantIds -> {
                    validateItems(r, validVariantIds);
                    return persist(r);
                });
    }

    private Mono<ManagerPurchaseResponse> persist(ManagerPurchaseRequest r) {
        // Orden fijo por variante: evita bloqueos cruzados entre transacciones
        List<ManagerPurchaseRequest.Item> sorted = r.getItems().stream()
                .sorted(Comparator.comparing(ManagerPurchaseRequest.Item::getVariantId))
                .toList();

        BigDecimal total = sorted.stream()
                .map(i -> i.getUnitCost().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Purchase purchase = new Purchase();
        purchase.setProductId(r.getProductId());
        purchase.setType(PurchaseType.UNIT);
        purchase.setBoxUnits(null);
        purchase.setTotalCost(total);
        purchase.setPurchaseDate(r.getPurchaseDate() != null ? r.getPurchaseDate() : LocalDateTime.now());
        purchase.setNotes(r.getNotes());

        return purchaseRepository.save(purchase).flatMap(saved -> {
            List<PurchaseItem> items = sorted.stream().map(i -> {
                PurchaseItem pi = new PurchaseItem();
                pi.setPurchaseId(saved.getId());
                pi.setVariantId(i.getVariantId());
                pi.setQuantity(i.getQuantity());
                pi.setRemaining(0);                 // suelto, sin BOX
                pi.setUnitCost(i.getUnitCost());
                return pi;
            }).toList();

            return purchaseItemRepository.saveAll(items).collectList()
                    .flatMap(savedItems ->
                            Flux.fromIterable(savedItems)
                                    .concatMap(si -> variantRepository
                                            .restoreStock(si.getVariantId(), si.getQuantity())
                                            .flatMap(rows -> rows > 0
                                                    ? Mono.<Void>empty()
                                                    : Mono.<Void>error(new ResourceNotFoundException(
                                                    "Variant not found with id: " + si.getVariantId()))))
                                    .then(Mono.just(toResponse(saved, savedItems))));
        });
    }

    public Mono<ManagerPurchaseResponse> getById(Long id) {
        return purchaseRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Purchase not found with id: " + id)))
                .flatMap(p -> purchaseItemRepository.findByPurchaseId(p.getId())
                        .collectList()
                        .map(items -> toResponse(p, items)));
    }

    public Flux<ManagerPurchaseResponse> getByProductId(Long productId) {
        return purchaseRepository.findByProductIdOrderByPurchaseDateDescIdDesc(productId)
                .concatMap(p -> purchaseItemRepository.findByPurchaseId(p.getId())
                        .collectList()
                        .map(items -> toResponse(p, items)));
    }

    // ── Validaciones ──────────────────────────────────────────────

    private void validateBasic(ManagerPurchaseRequest r) {
        if (r.getProductId() == null) throw bad("productId es obligatorio");
        if (r.getType() == null) throw bad("type es obligatorio (BOX o UNIT)");

        if (r.getType() == PurchaseType.BOX)
            throw bad("Los BOX se registran en POST /manager/purchases/box");

        if (r.getItems() == null || r.getItems().isEmpty())
            throw bad("Debe haber al menos una variante");

        for (var i : r.getItems()) {
            if (i.getUnitCost() == null || i.getUnitCost().signum() < 0)
                throw bad("unitCost debe ser 0 o mayor");
        }
    }

    private void validateItems(ManagerPurchaseRequest r, Set<Long> validVariantIds) {
        Set<Long> seen = new HashSet<>();
        for (var i : r.getItems()) {
            if (i.getVariantId() == null) throw bad("variantId es obligatorio");
            if (i.getQuantity() == null || i.getQuantity() < 1)
                throw bad("Cada cantidad debe ser mayor a 0");
            if (!seen.add(i.getVariantId()))
                throw bad("Variante repetida: " + i.getVariantId());
            if (!validVariantIds.contains(i.getVariantId()))
                throw bad("La variante " + i.getVariantId() + " no pertenece al producto o no está activa");
        }
    }

    private ResponseStatusException bad(String msg) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, msg);
    }

    private ManagerPurchaseResponse toResponse(Purchase p, List<PurchaseItem> items) {
        ManagerPurchaseResponse res = new ManagerPurchaseResponse();
        res.setId(p.getId());
        res.setProductId(p.getProductId());
        res.setType(p.getType());
        res.setBoxUnits(p.getBoxUnits());
        res.setTotalCost(p.getTotalCost());
        res.setPurchaseDate(p.getPurchaseDate());
        res.setNotes(p.getNotes());
        res.setItems(items.stream().map(i -> {
            var it = new ManagerPurchaseResponse.Item();
            it.setVariantId(i.getVariantId());
            it.setQuantity(i.getQuantity());
            it.setRemaining(i.getRemaining());
            it.setUnitCost(i.getUnitCost());
            return it;
        }).toList());
        return res;
    }
}