package com.ozmenyavuz.kafka;

import com.ozmenyavuz.dto.OrderCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


// Kafka’dan gelen mesajları dinler.
//Mesaj geldiğinde otomatik çalışır – bizim tetiklememize gerek yok.

@Service
public class KafkaConsumerService {

    @KafkaListener(
            topics = "order-events", // Her mesaj bir “konu (topic)”ya gönderilir. Dinlemek istiyorsan, “Ben şu topic’i dinliyorum” demelisin.
            groupId = "kafka101-json-group", // Aynı groupId‘ye sahip consumer’lar mesajları paylaşır / Farklı groupId’ler aynı mesajı ayrı ayrı alabilir.
            // biri loglasin, biri emai atsin, biri veritabanina eklesin. Farklı işler yapacaksa farklı groupId kullanılır.
            containerFactory = "kafkaListenerContainerFactory"
            // Bu listener, JSON mesajı almak için hazırlanmış special factory ile çalışacak.
    )
    public void consume(OrderCreatedEvent message) {
        System.out.println("📥 Kafka'dan JSON mesaj alındı: " + message);
    }

    // Bu method Kafka’dan gelen JSON mesajı alır,
    // ve bunu otomatik olarak OrderCreatedEvent Java nesnesine çeviri
    // Bu method:
    //	•	Biz hiçbir şey çağırmasak bile çalışır.
    //	•	Mesaj geldiğinde otomatik tetiklenir.
}