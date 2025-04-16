package com.ozmenyavuz.config;

import com.ozmenyavuz.dto.OrderCreatedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    // Producer için config =Kafkaya Mesaj gonderen Metot

    @Bean
    public ProducerFactory<String, OrderCreatedEvent> producerFactory() {

        Map<String, Object> config = new HashMap<>();

        // Kafka broker’a nereden ulaşacağını belirtir.
        //Bizim Kafka localhost (bilgisayarımızda) ve 9092 portunda çalışıyor.
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        // Kafka mesajlarının KEY’i nasıl baytlara dönüştürülecek? =String
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // Kafka mesajlarının VALUE’su nasıl bayta dönüştürülecek?
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        // Bu factory Yani RETURN, Kafka’ya mesaj göndermek için kullanılacak
        // olan KafkaTemplate’i yaratmakta kullanılacak.
        return new DefaultKafkaProducerFactory<>(config);
    }

    // KafkaTemplate, mesajı Kafka’ya gönderen asıl nesnedir.

    // | Ne yapar? | KafkaTemplate’i oluşturur |
    // | Nerede kullanılır? | KafkaProducerService içinde |
    // | Hangi ayarlarla çalışır? | producerFactory()’nin döndürdüğü config’lerle |
    // | Ne tür mesaj gönderir? | String key, OrderCreatedEvent JSON message |



    @Bean
    public KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());

        // KafkaTemplate bir “mesaj gönderen postacı” gibidir.
        // Ama bu postacının nasıl çalışacağı, hangi zarfı kullanacağı,
        // nereye gideceği gibi bilgiler producerFactory() içinde tanımlanır.
    }

    // Consumer için config = Kafka’nın dinleme (tüketme / mesaj alma) kısmına:
    // 📥 Yani Kafka’dan gelen JSON mesajları nasıl Java nesnesine çevrilir,
    // kim nasıl dinler? Bunu yöneten sınıf: consumerFactory().

    @Bean
    public ConsumerFactory<String, OrderCreatedEvent> consumerFactory() {

        // Kafkada gelen JSON mesajini, OrderCreatedEvent nesnesine cevirmesi icin deserializer(cozucu)

        JsonDeserializer<OrderCreatedEvent> deserializer = new JsonDeserializer<>(OrderCreatedEvent.class);
        deserializer.setRemoveTypeHeaders(false); //JSON mesajın üstünde özel Kafka tip etiketleri olabilir.
        deserializer.addTrustedPackages("*");// Kafka JSON mesajını hangi Java sınıfına çevireceğini bilmek ister.
        // Burada "*" diyerek tüm paketlere güveniyoruz (geliştirme ortamı için uygundur).
        deserializer.setUseTypeMapperForKey(true); // 	•	Kafka mesajlarında key (anahtar) bilgisi de özel bir yapıdaysa, doğru yorumlasın diye bu ayar yapılır.

        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka101-json-group"); //Bu Consumer hangi grubun bir parçası?
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
        // ConsumerFactory nesnesi oluturur

        // __________________

        // 🎯 Özet Tablo
        //
        //| Ne işe yarar? | Kafka’dan gelen JSON mesajları nasıl çözülecek, onu tanımlar |
        //| Ne döner? | Kafka mesajlarını deserialize edecek bir ConsumerFactory |
        //| Nerede kullanılır? | Listener Container Factory içinde |
        //| Key tipi? | String (ORD123 gibi) |
        //| Value tipi? | OrderCreatedEvent (bizim nesnemiz) |
        //| JSON mesajı nasıl nesneye döner? | JsonDeserializer sayesinde |
    }

    // Consumer’ların çalışmasını mümkün kılan kilit yapı
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;

        // Kafka bir haber merkezi gibi düşün.
        //Biz oraya “her siparişi dinleyeceğim” diye bir kulak (listener) yerleştiriyoruz.
        //
        //Ama bu kulak nasıl duyacak?
        //	•	Hangi kulak?
        //	•	Hangi dili konuşuyor?
        //	•	Hangi mesaj tipine alışık?
    }
}