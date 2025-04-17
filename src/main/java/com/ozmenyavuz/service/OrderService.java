package com.ozmenyavuz.service;

import com.ozmenyavuz.entity.OrderEntity;
import com.ozmenyavuz.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<OrderEntity> getByOrderId(String orderId) {
        return orderRepository.findAll().stream()
                .filter(o -> o.getOrderId().equals(orderId))
                .findFirst();
    }
}