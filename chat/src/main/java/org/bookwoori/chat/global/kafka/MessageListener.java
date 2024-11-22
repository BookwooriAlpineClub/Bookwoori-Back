package org.bookwoori.chat.global.kafka;

import lombok.RequiredArgsConstructor;
import org.bookwoori.chat.directMessage.domain.DirectMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageListener { //토픽에 발행된 이벤트를 가져와 처리

    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = KafkaConstants.DIRECT_CHAT_TOPIC)
    public void directChatListener(DirectMessage message) {
        messagingTemplate.convertAndSend("/topic/direct/" + message.getMessageRoomId(), message);
    }
}

