package org.bookwoori.chat.global.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bookwoori.chat.directMessage.domain.DirectMessage;
import org.bookwoori.chat.directMessage.dto.request.DirectMessageReactRequestDto;
import org.bookwoori.chat.directMessage.dto.request.DirectMessageReplyRequestDto;
import org.bookwoori.chat.directMessage.dto.request.DirectMessageSendRequestDto;
import org.bookwoori.chat.directMessage.dto.response.DirectMessageReplyResponseDto;
import org.bookwoori.chat.directMessage.repository.DirectMessageRepository;
import org.bookwoori.chat.global.common.ActionType;
import org.bookwoori.chat.global.common.CommonResponse;
import org.bookwoori.chat.global.common.EventType;
import org.bookwoori.chat.global.exception.CustomException;
import org.bookwoori.chat.global.exception.ErrorCode;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class MessageSender { //토픽에 이벤트를 발행

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final DirectMessageRepository directMessageRepository;

    public void sendDirectMessage(DirectMessageSendRequestDto requestDto) {
        DirectMessage directMessage = requestDto.toEntity();
        directMessageRepository.save(directMessage);
        kafkaTemplate.send(KafkaConstants.DIRECT_CHAT_TOPIC, directMessage);
    }

    public void reactToDirectMessage(DirectMessageReactRequestDto requestDto) {
        DirectMessage directMessage = directMessageRepository.findById(requestDto.id())
            .orElseThrow(() -> new CustomException(ErrorCode.DIRECT_MESSAGE_NOT_FOUND));
        if (requestDto.action().equals(ActionType.ADD)) {
            directMessage.addReaction(requestDto.emoji(), requestDto.memberId());
        } else if (requestDto.action().equals(ActionType.REMOVE)) {
            directMessage.removeReaction(requestDto.emoji(), requestDto.memberId());
        }
        directMessageRepository.save(directMessage);
        kafkaTemplate.send(KafkaConstants.DIRECT_CHAT_TOPIC, directMessage);
    }

    public void replyToDirectMessage(DirectMessageReplyRequestDto requestDto) {
        DirectMessage parentDirectMessage = directMessageRepository.findById(requestDto.parentId())
            .orElseThrow(() -> new CustomException(ErrorCode.DIRECT_MESSAGE_NOT_FOUND));
        DirectMessage directMessage = requestDto.toEntity();
        directMessageRepository.save(directMessage);

        DirectMessageReplyResponseDto responseDto = DirectMessageReplyResponseDto.from(
            directMessage, parentDirectMessage.getContent());

        kafkaTemplate.send(KafkaConstants.DIRECT_CHAT_EVENT_TOPIC,
            CommonResponse.fromDirectMessage(EventType.REPLY, directMessage.getMessageRoomId(),
                responseDto));
    }
}
