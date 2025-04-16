package com.ozmenyavuz.kafka;


import com.ozmenyavuz.dto.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;



//  KAFKAYA MESAJ GONDEREN SINIF

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;
    private static final String TOPIC_NAME = "order-events";

    public void sendOrder(OrderCreatedEvent order) {
        kafkaTemplate.send(TOPIC_NAME, order);
        System.out.println("✅ JSON Kafka mesajı gönderildi: " + order);
    }
}