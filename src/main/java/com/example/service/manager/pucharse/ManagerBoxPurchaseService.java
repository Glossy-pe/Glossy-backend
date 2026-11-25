package com.example.service.manager.pucharse;
import com.example.dto.manager.request.box_presentation.ManagerBoxPresentationRequest;
import com.example.dto.manager.request.pucharse.ManagerBoxPurchaseRequest;
import com.example.dto.manager.response.box_presentation.ManagerBoxPresentationResponse;
import com.example.dto.manager.response.pucharse.ManagerPurchaseResponse;
import com.example.entity.BoxPresentation;
import com.example.entity.ProductVariant;
import com.example.entity.Purchase;
import com.example.entity.PurchaseItem;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.*;
import com.example.utils.PurchaseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;


@Service
public class ManagerBoxPurchaseService {

    @Autowired private PurchaseRepository purchaseRepository;
    @Autowired private PurchaseItemRepository purchaseItemRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private VariantRepository variantRepository;
    @Autowired private BoxPresentationRepository boxPresentationRepository;

    @Transactional
    public Mono<List<ManagerPurchaseResponse>> registerBoxes(ManagerBoxPurchaseRequest r) {
        return Mono.defer(() -> {
                    validate(r);
                    return productRepository.existsById(r.getProductId());
                })
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.<Tuple2<BoxPresentation, List<ProductVariant>>>error(
                                new ResourceNotFoundException("Product not found with id: " + r.getProductId()));
                    }
                    Mono<BoxPresentation> presentation = boxPresentationRepository
                            .findByProductId(r.getProductId())
                            .switchIfEmpty(Mono.error(bad(
                                    "El producto no tiene Presentación BOX. Defínela primero.")));
                    Mono<List<ProductVariant>> variants = variantRepository
                            .findByProductId(r.getProductId())
                            .filter(v -> Boolean.TRUE.equals(v.getActive())
                                    && !Boolean.TRUE.equals(v.getDeleted()))
                            .sort(Comparator.comparing(ProductVariant::getId))
                            .collectList();
                    return Mono.zip(presentation, variants);
                })
                .flatMap(t -> persist(r, t.getT1(), t.getT2()));
    }

    private Mono<List<ManagerPurchaseResponse>> persist(
            ManagerBoxPurchaseRequest r, BoxPresentation pres, List<ProductVariant> variants) {

        int total = pres.getTotalUnits();
        int n = variants.size();
        if (n == 0) throw bad("El producto no tiene variantes activas");
        if (total % n != 0)
            throw bad("Las " + total + " unidades del BOX no se dividen exacto entre las "
                    + n + " variantes activas. Corrige la Presentación BOX o las variantes.");

        int perVariant = total / n;
        BigDecimal unitCost = r.getCostPerBox()
                .divide(BigDecimal.valueOf(total), 4, RoundingMode.HALF_UP);
        LocalDateTime date = r.getPurchaseDate() != null ? r.getPurchaseDate() : LocalDateTime.now();

        return Flux.range(0, r.getBoxCount())
                .concatMap(i -> saveBox(r, total, date, variants, perVariant, unitCost))
                .collectList()
                .flatMap(boxes -> Flux.fromIterable(variants)   // ya ordenadas por id
                        .concatMap(v -> variantRepository
                                .restoreStock(v.getId(), perVariant * r.getBoxCount())
                                .flatMap(rows -> rows > 0
                                        ? Mono.<Void>empty()
                                        : Mono.<Void>error(new ResourceNotFoundException(
                                        "Variant not found with id: " + v.getId()))))
                        .then(Mono.just(boxes)));
    }

    private Mono<ManagerPurchaseResponse> saveBox(
            ManagerBoxPurchaseRequest r, int total, LocalDateTime date,
            List<ProductVariant> variants, int perVariant, BigDecimal unitCost) {

        Purchase p = new Purchase();
        p.setProductId(r.getProductId());
        p.setType(PurchaseType.BOX);
        p.setBoxUnits(total);
        p.setTotalCost(r.getCostPerBox());
        p.setPurchaseDate(date);
        p.setNotes(r.getNotes());

        return purchaseRepository.save(p).flatMap(saved -> {
            List<PurchaseItem> items = variants.stream().map(v -> {
                PurchaseItem pi = new PurchaseItem();
                pi.setPurchaseId(saved.getId());
                pi.setVariantId(v.getId());
                pi.setQuantity(perVariant);
                pi.setRemaining(perVariant);   // BOX intacto
                pi.setUnitCost(unitCost);
                return pi;
            }).toList();
            return purchaseItemRepository.saveAll(items).collectList()
                    .map(savedItems -> toResponse(saved, savedItems));
        });
    }

    private void validate(ManagerBoxPurchaseRequest r) {
        if (r.getProductId() == null) throw bad("productId es obligatorio");
        if (r.getBoxCount() == null || r.getBoxCount() < 1 || r.getBoxCount() > 100)
            throw bad("boxCount debe estar entre 1 y 100");
        if (r.getCostPerBox() == null || r.getCostPerBox().signum() < 0)
            throw bad("costPerBox debe ser 0 o mayor");
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