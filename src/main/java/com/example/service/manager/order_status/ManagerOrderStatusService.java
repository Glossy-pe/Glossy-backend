package com.example.service.manager.order_status;

import com.example.dto.manager.response.order_status.ManagerOrderStatusResponse;
import com.example.mapper.manager.ManagerOrderStatusMapper;
import com.example.repository.OrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ManagerOrderStatusService {

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private ManagerOrderStatusMapper managerOrderStatusMapper;

    public Flux<ManagerOrderStatusResponse> getAll() {
        return orderStatusRepository.findAll().map(managerOrderStatusMapper::toResponse);
    }
}
