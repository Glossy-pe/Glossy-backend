package com.example.controller.Manager;

import com.example.dto.manager.response.order_item.ManagerOrderItemResponse;
import com.example.dto.manager.response.order_status.ManagerOrderStatusResponse;
import com.example.service.manager.order_item.ManagerOrderItemService;
import com.example.service.manager.order_status.ManagerOrderStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/manager/order-status")
@RequiredArgsConstructor
public class ManagerOrderStatusController {
    @Autowired
    private ManagerOrderStatusService managerOrderStatusService;

    @GetMapping
    public Flux<ManagerOrderStatusResponse> getAll() {
        return managerOrderStatusService.getAll();
    }
}
