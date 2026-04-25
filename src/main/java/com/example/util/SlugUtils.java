package com.example.util;

import com.example.entity.Product;
import com.example.repository.ProductRepository;
import java.text.Normalizer;
import java.util.Optional;

public class SlugUtils {

    public static String toSlug(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
            .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
            .toLowerCase()
            .replaceAll("[^a-z0-9\\s-]", "")
            .trim()
            .replaceAll("\\s+", "-");
    }

    public static String toUniqueSlug(String input, ProductRepository productRepository) {
        String base = toSlug(input);
        String slug = base;
        int counter = 2;
        while (productRepository.findBySlug(slug).isPresent()) {
            slug = base + "-" + counter;
            counter++;
        }
        return slug;
    }

    public static String toUniqueSlugForUpdate(String input, Long productId,
                                                ProductRepository productRepository) {
        String base = toSlug(input);
        String slug = base;
        int counter = 2;
        while (true) {
            Optional<Product> existing = productRepository.findBySlug(slug);
            if (existing.isEmpty() || existing.get().getId().equals(productId)) break;
            slug = base + "-" + counter;
            counter++;
        }
        return slug;
    }
}