package com.example.service.manager.images;

import com.cloudinary.Cloudinary;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;

@Service
public class CloudinaryService {

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    private Cloudinary cloudinary;

    @PostConstruct
    public void init() {
        cloudinary = new Cloudinary(Map.of(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true
        ));
    }

    // Extrae el public_id de una URL de Cloudinary
    // "https://res.cloudinary.com/dqyqtgkdk/image/upload/v1234567/products/5/abc123.jpg"
    // → "products/5/abc123"
    public String extractPublicId(String url) {
        String marker = "/upload/";
        int uploadIndex = url.indexOf(marker);
        if (uploadIndex == -1) return null;

        String afterUpload = url.substring(uploadIndex + marker.length());

        // Saltar la versión si existe (v1234567/)
        if (afterUpload.startsWith("v") && afterUpload.contains("/")) {
            afterUpload = afterUpload.substring(afterUpload.indexOf("/") + 1);
        }

        // Quitar extensión (.jpg, .png, etc.)
        int dotIndex = afterUpload.lastIndexOf(".");
        return dotIndex != -1 ? afterUpload.substring(0, dotIndex) : afterUpload;
    }

    public Mono<Void> delete(String url, String resourceType) {
        String publicId = extractPublicId(url);
        if (publicId == null) return Mono.empty();

        // "video" → "video", cualquier otra cosa → "image"
        String cloudinaryResourceType = "video".equals(resourceType) ? "video" : "image";

        return Mono.fromCallable(() -> {
                    cloudinary.uploader().destroy(publicId, Map.of("resource_type", cloudinaryResourceType));
                    return null;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }
}
