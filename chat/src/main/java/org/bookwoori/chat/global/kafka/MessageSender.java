package org.bookwoori.chat.global.kafka;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bookwoori.chat.directMessage.domain.DirectMessage;
import org.bookwoori.chat.directMessage.dto.request.DirectMessageSendRequestDto;
import org.bookwoori.chat.directMessage.repository.DirectMessageRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class MessageSender { //토픽에 이벤트를 발행

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final DirectMessageRepository directMessageRepository;

    public void sendDirectMessage(DirectMessageSendRequestDto requestDto) {
        DirectMessage directMessage = requestDto.toEntity(LocalDateTime.now());
        directMessageRepository.save(directMessage);
        kafkaTemplate.send(KafkaConstants.DIRECT_CHAT_TOPIC, directMessage);
    }
}
