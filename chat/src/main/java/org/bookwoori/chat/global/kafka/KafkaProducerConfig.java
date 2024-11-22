package org.bookwoori.chat.global.kafka;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    /*
     * Producer 생성을 위한 ProducerFactory 설정
     * BOOTSTRAP_SERVERS_CONFIG : Kafka가 실행되는 주소 설정
     * KEY_SERIALIZER_CLASS_CONFIG, VALUE_SERIALIZER_CLASS_CONFIG : Kafka로 보내는 데이터의 키와 값을 직렬화. 문자열을 넘길 때는 StringSerializer.class, JSON 데이터를 넘길 때는 JsonSerializer.class를 적어주면 됨
     */
    @Bean
    public Map<String, Object> producerFactoryConfig() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return configProps;
    }

    @Bean
    public ProducerFactory<String, Object> producerFactoryForDirect() {
        return new DefaultKafkaProducerFactory<>(producerFactoryConfig());
    }

    /*
     * 이벤트 생성을 편리하게 할 수 있도록 도와주는 Template
     */
    @Bean
    KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactoryForDirect());
    }
}
