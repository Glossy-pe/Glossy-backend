package com.example.controller.Manager;
import com.example.dto.manager.request.pucharse.ManagerPurchaseRequest;
import com.example.dto.manager.request.sales.ManagerSaleTemplateRequest;
import com.example.dto.manager.response.pucharse.ManagerPurchaseResponse;
import com.example.dto.manager.response.sales.ManagerSaleTemplateResponse;
import com.example.service.manager.pucharse.ManagerPurchaseService;
import com.example.service.manager.sale.ManagerSaleTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/manager/sale-templates")
@RequiredArgsConstructor
public class ManagerSaleTemplateController {

    @Autowired private ManagerSaleTemplateService service;

    @GetMapping
    public Flux<ManagerSaleTemplateResponse> getByProduct(@RequestParam Long productId) {
        return service.getByProductId(productId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ManagerSaleTemplateResponse> create(@RequestBody ManagerSaleTemplateRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public Mono<ManagerSaleTemplateResponse> update(
            @PathVariable Long id, @RequestBody ManagerSaleTemplateRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deactivate(@PathVariable Long id) {
        return service.deactivate(id);
    }
}