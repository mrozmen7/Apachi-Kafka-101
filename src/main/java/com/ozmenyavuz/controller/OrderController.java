package com.ozmenyavuz.controller;

import com.ozmenyavuz.dto.OrderCreatedEvent;
import com.ozmenyavuz.entity.OrderEntity;
import com.ozmenyavuz.kafka.KafkaProducerService;
import com.ozmenyavuz.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final KafkaProducerService producerService;
    private final OrderService orderService;

    // ✅ 1. SIPARIŞ OLUŞTUR (Kafka'ya JSON mesaj gönder)
    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderCreatedEvent order) {
        producerService.sendOrder(order);  // Kafka'ya gönder
        return ResponseEntity.ok("✅ Sipariş Kafka'ya gönderildi.");
    }

    // ✅ 2. TÜM SİPARİŞLERİ GETİR (DB üzerinden)
    @GetMapping
    public ResponseEntity<List<OrderEntity>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // ✅ 3. BELİRLİ BİR SİPARİŞİ GETİR (orderId ile)
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderEntity> getOrderByOrderId(@PathVariable String orderId) {
        return orderService.getByOrderId(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    // 4. Delete Islemleri

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable String orderId) {
        boolean deleted = orderService.deleteByOrderId(orderId);
        if (deleted) {
            return ResponseEntity.ok("✅ Sipariş silindi: " + orderId);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}