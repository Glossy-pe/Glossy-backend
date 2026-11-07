package com.example.service.guest.kit;

import com.example.dto.guest.response.kit.KitItemPublicResponse;
import com.example.dto.guest.response.kit.KitPublicResponse;
import com.example.repository.KitItemRepository;
import com.example.repository.KitRepository;
import com.example.service.guest.product.GuestProductRestFullService;import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class KitPublicService {

    @Autowired
    private KitRepository kitRepository;

    @Autowired
    private KitItemRepository kitItemRepository;

    @Autowired
    private GuestProductRestFullService guestProductRestFullService;

    public Mono<KitPublicResponse> getKit(Long kitId) {
        return kitRepository.findById(kitId)
                .filter(kit -> Boolean.TRUE.equals(kit.getActive()))
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Kit no disponible")))
                .flatMap(kit ->
                        kitItemRepository.findByKitIdOrderByPosition(kitId)
                                .flatMapSequential(item ->
                                        guestProductRestFullService.getProductByIdFull(item.getProductId())
                                                .map(product -> {
                                                    KitItemPublicResponse dto = new KitItemPublicResponse();
                                                    dto.setPosition(item.getPosition());
                                                    dto.setProduct(product);
                                                    return dto;
                                                }))
                                .collectList()
                                .map(items -> {
                                    KitPublicResponse response = new KitPublicResponse();
                                    response.setId(kit.getId());
                                    response.setName(kit.getName());
                                    response.setItems(items);
                                    return response;
                                })
                );
    }
}