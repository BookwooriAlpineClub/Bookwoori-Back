package org.bookwoori.chat.global.kafka;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;
    @Value("${spring.kafka.topic.direct-chat}")
    private String directChatTopic;

    @Value("${spring.kafka.topic.direct-chat-event}")
    private String directChatEventTopic;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    /*
     * 토픽 자동 생성 및 주입
     */
    @Bean
    public NewTopic directChatTopic() {
        return new NewTopic(directChatTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic directChatEventTopic() {
        return new NewTopic(directChatEventTopic, 1, (short) 1);
    }

}