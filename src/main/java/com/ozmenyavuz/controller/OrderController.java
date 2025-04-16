package com.ozmenyavuz.controller;

import com.ozmenyavuz.dto.OrderCreatedEvent;
import com.ozmenyavuz.kafka.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final KafkaProducerService producerService;

    @PostMapping
    public String createOrder(@RequestBody OrderCreatedEvent order) {
        producerService.sendOrder(order);
        return "Sipariş Kafka'ya gönderildi.";
    }
}