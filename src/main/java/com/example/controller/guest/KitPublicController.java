package com.example.controller.guest;

import com.example.dto.guest.response.kit.KitPublicResponse;
import com.example.service.guest.kit.KitPublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/public/kits")
public class KitPublicController {

    @Autowired
    private KitPublicService kitPublicService;

    @GetMapping("/{id}")
    public Mono<KitPublicResponse> getKit(@PathVariable Long id) {
        return kitPublicService.getKit(id);
    }
}
