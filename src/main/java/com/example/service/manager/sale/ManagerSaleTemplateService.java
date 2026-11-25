package com.example.service.manager.sale;

import com.example.dto.manager.request.sales.ManagerSaleTemplateRequest;
import com.example.dto.manager.response.sales.ManagerSaleTemplateResponse;
import com.example.entity.BoxPresentation;
import com.example.entity.ProductVariant;
import com.example.entity.SaleTemplate;
import com.example.entity.SaleTemplateItem;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.BoxPresentationRepository;
import com.example.repository.ProductRepository;
import com.example.repository.SaleTemplateItemRepository;
import com.example.repository.SaleTemplateRepository;
import com.example.repository.VariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ManagerSaleTemplateService {

    @Autowired private SaleTemplateRepository templateRepository;
    @Autowired private SaleTemplateItemRepository itemRepository;
    @Autowired private BoxPresentationRepository boxPresentationRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private VariantRepository variantRepository;

    private record Ctx(Map<Long, ProductVariant> activeVariants, BoxPresentation presentation) {}

    // ── Lectura ───────────────────────────────────────────────────
    public Flux<ManagerSaleTemplateResponse> getByProductId(Long productId) {
        return allVariantsById(productId).flatMapMany(vmap ->
                templateRepository.findByProductIdOrderByIdAsc(productId)
                        .concatMap(t -> itemRepository.findByTemplateId(t.getId())
                                .collectList()
                                .map(items -> toResponse(t, items, vmap))));
    }

    // ── Crear ─────────────────────────────────────────────────────
    @Transactional
    public Mono<ManagerSaleTemplateResponse> create(ManagerSaleTemplateRequest r) {
        return Mono.defer(() -> {
                    if (r.getProductId() == null) throw bad("productId es obligatorio");
                    validateBasic(r);
                    return loadCtx(r.getProductId());
                })
                .flatMap(ctx -> {
                    validateItems(r, ctx);
                    SaleTemplate t = new SaleTemplate();
                    t.setProductId(r.getProductId());
                    t.setName(r.getName().trim());
                    t.setUnitPrice(r.getUnitPrice());
                    t.setActive(r.getActive() == null || r.getActive());
                    return templateRepository.save(t)
                            .flatMap(saved -> saveItems(saved.getId(), r.getItems())
                                    .flatMap(items -> allVariantsById(saved.getProductId())
                                            .map(vmap -> toResponse(saved, items, vmap))));
                })
                .onErrorMap(DuplicateKeyException.class,
                        e -> bad("Ya existe una plantilla con ese nombre para este producto"));
    }

    // ── Actualizar ────────────────────────────────────────────────
    @Transactional
    public Mono<ManagerSaleTemplateResponse> update(Long id, ManagerSaleTemplateRequest r) {
        return templateRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Template not found with id: " + id)))
                .flatMap(t -> Mono.defer(() -> {
                            validateBasic(r);
                            return loadCtx(t.getProductId());
                        })
                        .flatMap(ctx -> {
                            validateItems(r, ctx);
                            t.setName(r.getName().trim());
                            t.setUnitPrice(r.getUnitPrice());
                            if (r.getActive() != null) t.setActive(r.getActive());
                            return templateRepository.save(t);
                        }))
                .flatMap(saved -> itemRepository.deleteAllByTemplate(saved.getId())
                        .then(saveItems(saved.getId(), r.getItems()))
                        .flatMap(items -> allVariantsById(saved.getProductId())
                                .map(vmap -> toResponse(saved, items, vmap))))
                .onErrorMap(DuplicateKeyException.class,
                        e -> bad("Ya existe una plantilla con ese nombre para este producto"));
    }

    // ── "Eliminar" = desactivar (las ventas futuras la referenciarán) ──
    @Transactional
    public Mono<Void> deactivate(Long id) {
        return templateRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Template not found with id: " + id)))
                .flatMap(t -> { t.setActive(false); return templateRepository.save(t); })
                .then();
    }

    // ── Helpers ───────────────────────────────────────────────────
    private Mono<List<SaleTemplateItem>> saveItems(Long templateId, List<ManagerSaleTemplateRequest.Item> req) {
        List<SaleTemplateItem> items = req.stream().map(i -> {
            SaleTemplateItem it = new SaleTemplateItem();
            it.setTemplateId(templateId);
            it.setVariantId(i.getVariantId());
            it.setQuantity(i.getQuantity());
            return it;
        }).toList();
        return itemRepository.saveAll(items).collectList();
    }

    private Mono<Ctx> loadCtx(Long productId) {
        return productRepository.existsById(productId).flatMap(exists -> {
            if (!exists) return Mono.<Ctx>error(new ResourceNotFoundException("Product not found with id: " + productId));
            Mono<Map<Long, ProductVariant>> variants = variantRepository.findByProductId(productId)
                    .filter(v -> Boolean.TRUE.equals(v.getActive()) && !Boolean.TRUE.equals(v.getDeleted()))
                    .collectMap(ProductVariant::getId);
            Mono<BoxPresentation> pres = boxPresentationRepository.findByProductId(productId)
                    .switchIfEmpty(Mono.error(bad("Define primero la Presentación BOX del producto")));
            return Mono.zip(variants, pres).map(t -> new Ctx(t.getT1(), t.getT2()));
        });
    }

    private Mono<Map<Long, ProductVariant>> allVariantsById(Long productId) {
        return variantRepository.findByProductId(productId).collectMap(ProductVariant::getId);
    }

    private void validateBasic(ManagerSaleTemplateRequest r) {
        if (r.getName() == null || r.getName().isBlank()) throw bad("name es obligatorio");
        if (r.getName().trim().length() > 60) throw bad("name no puede superar 60 caracteres");
        if (r.getUnitPrice() == null || r.getUnitPrice().signum() < 0) throw bad("unitPrice debe ser 0 o mayor");
        if (r.getItems() == null || r.getItems().isEmpty()) throw bad("Debe incluir al menos una variante");
    }

    private void validateItems(ManagerSaleTemplateRequest r, Ctx ctx) {
        int n = ctx.activeVariants().size();
        int total = ctx.presentation().getTotalUnits();
        // Máximo por variante dentro de un BOX (si la presentación es válida)
        Integer maxPerVariant = (n > 0 && total % n == 0) ? total / n : null;

        Set<Long> seen = new HashSet<>();
        for (var i : r.getItems()) {
            if (i.getVariantId() == null) throw bad("variantId es obligatorio");
            if (i.getQuantity() == null || i.getQuantity() < 1)
                throw bad("Cada cantidad debe ser mayor a 0");
            if (!seen.add(i.getVariantId()))
                throw bad("Variante repetida: " + i.getVariantId());
            if (!ctx.activeVariants().containsKey(i.getVariantId()))
                throw bad("La variante " + i.getVariantId() + " no pertenece al producto o no está activa");
            if (maxPerVariant != null && i.getQuantity() > maxPerVariant)
                throw bad("La variante " + i.getVariantId() + " solo trae " + maxPerVariant + " por BOX");
        }
    }

    private ManagerSaleTemplateResponse toResponse(
            SaleTemplate t, List<SaleTemplateItem> items, Map<Long, ProductVariant> vmap) {

        BigDecimal reference = BigDecimal.ZERO;
        int totalUnits = 0;
        List<ManagerSaleTemplateResponse.Item> out = new ArrayList<>();

        for (SaleTemplateItem i : items) {
            ProductVariant v = vmap.get(i.getVariantId());
            BigDecimal variantPrice = (v != null && v.getPrice() != null) ? v.getPrice() : BigDecimal.ZERO;
            reference = reference.add(variantPrice.multiply(BigDecimal.valueOf(i.getQuantity())));
            totalUnits += i.getQuantity();

            var it = new ManagerSaleTemplateResponse.Item();
            it.setVariantId(i.getVariantId());
            it.setToneName(v != null ? v.getToneName() : null);
            it.setQuantity(i.getQuantity());
            it.setUnitPrice(variantPrice);
            out.add(it);
        }

        // Total = precio unitario de la plantilla × unidades que incluye
        BigDecimal totalPrice = t.getUnitPrice()
                .multiply(BigDecimal.valueOf(totalUnits))
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal discount = reference.subtract(totalPrice);
        BigDecimal percent = reference.signum() > 0
                ? discount.multiply(BigDecimal.valueOf(100)).divide(reference, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        ManagerSaleTemplateResponse res = new ManagerSaleTemplateResponse();
        res.setId(t.getId());
        res.setProductId(t.getProductId());
        res.setName(t.getName());
        res.setUnitPrice(t.getUnitPrice());
        res.setTotalPrice(totalPrice);
        res.setReferencePrice(reference);
        res.setDiscount(discount);
        res.setDiscountPercent(percent);
        res.setActive(t.getActive());
        res.setTotalUnits(totalUnits);
        res.setItems(out);
        return res;
    }

    private ResponseStatusException bad(String msg) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, msg);
    }
}