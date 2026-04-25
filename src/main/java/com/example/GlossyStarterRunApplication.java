package com.example;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import com.example.service.MeilisearchService;
import com.example.service.ProductService;

@SpringBootApplication
@EnableAsync
public class GlossyStarterRunApplication {
    public static void main(String[] args) {
        SpringApplication.run(GlossyStarterRunApplication.class, args);
    }


    @Bean
    public ApplicationRunner initMeilisearch(MeilisearchService meilisearchService, ProductService productService) {
        return args -> {
            meilisearchService.configureIndex();
            productService.reindexAll();  // 👈 sincroniza al arrancar
        };
    }
}
