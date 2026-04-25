package com.example.service;

import com.example.dtos.response.full.ProductResponseFull;
import com.example.dtos.response.full.VariantResponseFull;
import com.example.dtos.document.ProductDocument;
import com.example.dtos.response.LabelResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.SearchResult;
import com.meilisearch.sdk.model.Searchable;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeilisearchService {

    private final Client meilisearchClient;
    private final ObjectMapper objectMapper;

    @Value("${meilisearch.index:products}")
    private String indexName;

    // ─── Índice con creación lazy ───────────────────────────────────────────

    private Index getIndex() {
        try {
            return meilisearchClient.index(indexName);
        } catch (Exception e) {
            log.error("Error obteniendo índice Meilisearch", e);
            throw new RuntimeException("Meilisearch no disponible", e);
        }
    }

    // ─── Configurar campos filtrables (llamar una vez al arrancar) ──────────

    public void configureIndex() {
        try {
            Index index = getIndex();
            // Campos por los que podrás filtrar
            index.updateFilterableAttributesSettings(
                new String[]{"categoryId", "labelIds", "active", "minPrice", "maxPrice"}
            );
            // Campos por los que podrás ordenar
            index.updateSortableAttributesSettings(
                new String[]{"minPrice", "maxPrice", "name"}
            );
            log.info("Meilisearch index '{}' configurado correctamente", indexName);
        } catch (Exception e) {
            log.warn("No se pudo configurar el índice Meilisearch: {}", e.getMessage());
        }
    }

    // ─── Convertir ProductResponseFull → ProductDocument ───────────────────

    public ProductDocument toDocument(ProductResponseFull p) {
        List<String> labelNames = p.getLabels() == null ? List.of() :
            p.getLabels().stream().map(LabelResponse::getName).collect(Collectors.toList());

        List<Long> labelIds = p.getLabels() == null ? List.of() :
            p.getLabels().stream().map(LabelResponse::getId).collect(Collectors.toList());

        BigDecimal minPrice = BigDecimal.ZERO;
        BigDecimal maxPrice = BigDecimal.ZERO;
        int totalStock = 0;

        if (p.getVariants() != null && !p.getVariants().isEmpty()) {
            minPrice = p.getVariants().stream()
                .map(VariantResponseFull::getPrice)
                .filter(price -> price != null)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

            maxPrice = p.getVariants().stream()
                .map(VariantResponseFull::getPrice)
                .filter(price -> price != null)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

            totalStock = p.getVariants().stream()
                .mapToInt(v -> v.getStock() != null ? v.getStock() : 0)
                .sum();
        }

        return ProductDocument.builder()
            .id(p.getId())
            .name(p.getName())
            .description(p.getDescription())
            .fullDescription(p.getFullDescription())
            .active(p.getActive())
            .categoryId(p.getCategoryId())
            .labelNames(labelNames)
            .labelIds(labelIds)
            .minPrice(minPrice)
            .maxPrice(maxPrice)
            .totalStock(totalStock)
            .build();
    }

    // ─── Indexar un producto ────────────────────────────────────────────────

    public void indexProduct(ProductResponseFull product) {
        try {
            ProductDocument doc = toDocument(product);
            String json = objectMapper.writeValueAsString(List.of(doc));
            getIndex().addDocuments(json, "id");
            log.debug("Producto {} indexado en Meilisearch", product.getId());
        } catch (Exception e) {
            log.error("Error indexando producto {}: {}", product.getId(), e.getMessage());
        }
    }

    // ─── Eliminar un producto ───────────────────────────────────────────────

    public void deleteProduct(Long productId) {
        try {
            getIndex().deleteDocument(String.valueOf(productId));
            log.debug("Producto {} eliminado de Meilisearch", productId);
        } catch (Exception e) {
            log.error("Error eliminando producto {} de Meilisearch: {}", productId, e.getMessage());
        }
    }

    // ─── Indexar lista completa (re-index) ──────────────────────────────────

    public void reindexAll(List<ProductResponseFull> products) {
        try {
            List<ProductDocument> docs = products.stream()
                .map(this::toDocument)
                .collect(Collectors.toList());
            String json = objectMapper.writeValueAsString(docs);
            getIndex().addDocuments(json, "id");
            log.info("Re-indexados {} productos en Meilisearch", docs.size());
        } catch (Exception e) {
            log.error("Error en re-indexación masiva: {}", e.getMessage());
        }
    }

    // ─── Buscar ─────────────────────────────────────────────────────────────

    public List<Long> search(String q, int limit, int offset,
                              Long categoryId, Long labelId) {
        try {
            SearchRequest.SearchRequestBuilder builder = SearchRequest.builder()
                .q(q)
                .limit(limit)
                .offset(offset);

            // Filtros opcionales
            if (categoryId != null && labelId != null) {
                builder.filter(new String[]{
                    "categoryId = " + categoryId,
                    "labelIds = " + labelId
                });
            } else if (categoryId != null) {
                builder.filter(new String[]{"categoryId = " + categoryId});
            } else if (labelId != null) {
                builder.filter(new String[]{"labelIds = " + labelId});
            }

            Searchable result = getIndex().search(builder.build());

            return result.getHits().stream()
            .map(hit -> {
                Object rawId = hit.get("id");
                if (rawId instanceof Integer) return ((Integer) rawId).longValue();
                if (rawId instanceof Long) return (Long) rawId;
                if (rawId instanceof Double) return ((Double) rawId).longValue(); // 👈 agrega esto
                return Long.parseLong(rawId.toString().replace(".0", "")); // 👈 y esto
            })
            .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error buscando en Meilisearch: {}", e.getMessage());
            return List.of();
        }
    }
}