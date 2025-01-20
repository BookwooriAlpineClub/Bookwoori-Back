package org.bookwoori.chat.global.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bookwoori.chat.domain.channelMessage.dto.request.ChannelMessageModifyRequestDto;
import org.bookwoori.chat.domain.channelMessage.dto.request.ChannelMessageReactRequestDto;
import org.bookwoori.chat.domain.channelMessage.dto.request.ChannelMessageReplyRequestDto;
import org.bookwoori.chat.domain.channelMessage.dto.request.ChannelMessageSendRequestDto;
import org.bookwoori.chat.domain.channelMessage.entity.ChannelMessage;
import org.bookwoori.chat.domain.channelMessage.repository.ChannelMessageRepository;
import org.bookwoori.chat.domain.directMessage.dto.request.DirectMessageDeleteRequestDto;
import org.bookwoori.chat.domain.directMessage.dto.request.DirectMessageModifyRequestDto;
import org.bookwoori.chat.domain.directMessage.dto.request.DirectMessageReactRequestDto;
import org.bookwoori.chat.domain.directMessage.dto.request.DirectMessageReplyRequestDto;
import org.bookwoori.chat.domain.directMessage.dto.request.DirectMessageSendRequestDto;
import org.bookwoori.chat.domain.directMessage.entity.DirectMessage;
import org.bookwoori.chat.domain.directMessage.repository.DirectMessageRepository;
import org.bookwoori.chat.global.common.ActionType;
import org.bookwoori.chat.global.common.EventType;
import org.bookwoori.chat.global.common.MessageType;
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
    private final ChannelMessageRepository channelMessageRepository;

    public void sendDirectMessage(DirectMessageSendRequestDto requestDto, Long memberId) {
        DirectMessage directMessage = requestDto.toEntity(memberId);
        directMessageRepository.save(directMessage);
        kafkaTemplate.send(KafkaConstants.DIRECT_CHAT_TOPIC, directMessage);
    }

    public void reactToDirectMessage(DirectMessageReactRequestDto requestDto, Long memberId) {
        DirectMessage directMessage = directMessageRepository.findById(requestDto.id())
            .orElseThrow(() -> new CustomException(ErrorCode.DIRECT_MESSAGE_NOT_FOUND));
        if (requestDto.action().equals(ActionType.ADD)) {
            directMessage.addReaction(requestDto.emoji(), memberId);
        } else if (requestDto.action().equals(ActionType.REMOVE)) {
            directMessage.removeReaction(requestDto.emoji(), memberId);
        }
        directMessage.setEventType(EventType.REACT);
        directMessage.setTargetEmoji(requestDto.emoji());
        directMessageRepository.save(directMessage);
        kafkaTemplate.send(KafkaConstants.DIRECT_CHAT_EVENT_TOPIC, directMessage);
    }

    public void replyToDirectMessage(DirectMessageReplyRequestDto requestDto, Long memberId) {
        DirectMessage parentDirectMessage = directMessageRepository.findById(requestDto.parentId())
            .orElseThrow(() -> new CustomException(ErrorCode.DIRECT_MESSAGE_NOT_FOUND));
        DirectMessage directMessage = requestDto.toEntity(memberId,
            parentDirectMessage.getContent());
        directMessageRepository.save(directMessage);
        kafkaTemplate.send(KafkaConstants.DIRECT_CHAT_EVENT_TOPIC, directMessage);
    }

    public void modifyDirectMessage(DirectMessageModifyRequestDto requestDto, Long memberId) {
        DirectMessage directMessage = directMessageRepository.findById(requestDto.id())
            .orElseThrow(() -> new CustomException(ErrorCode.DIRECT_MESSAGE_NOT_FOUND));

        // TEXT 타입 메시지만 수정 가능
        if (!directMessage.getType().equals(MessageType.TEXT)) {
            throw new CustomException(ErrorCode.INVALID_MESSAGE_TYPE_EXCEPTION);
        }
        // 메시지 전송자만 수정 가능
        if (!directMessage.getMemberId().equals(memberId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        directMessage.modifyContent(requestDto.content());
        directMessage.setEventType(EventType.MODIFY);
        directMessageRepository.save(directMessage);
        kafkaTemplate.send(KafkaConstants.DIRECT_CHAT_EVENT_TOPIC, directMessage);
    }

    public void deleteDirectMessage(DirectMessageDeleteRequestDto requestDto, Long memberId) {
        DirectMessage directMessage = directMessageRepository.findById(requestDto.id())
            .orElseThrow(() -> new CustomException(ErrorCode.DIRECT_MESSAGE_NOT_FOUND));

        // 메시지 전송자만 삭제 가능
        if (!directMessage.getMemberId().equals(memberId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        directMessage.setEventType(EventType.DELETE);
        directMessageRepository.delete(directMessage);
        kafkaTemplate.send(KafkaConstants.DIRECT_CHAT_EVENT_TOPIC, directMessage);

    }

    public void sendChannelMessage(ChannelMessageSendRequestDto requestDto, Long memberId) {
        ChannelMessage channelMessage = requestDto.toEntity(memberId);
        channelMessageRepository.save(channelMessage);
        kafkaTemplate.send(KafkaConstants.CHANNEL_CHAT_TOPIC, channelMessage);
    }

    public void reactToChannelMessage(ChannelMessageReactRequestDto requestDto, Long memberId) {
        ChannelMessage channelMessage = channelMessageRepository.findById(requestDto.id())
            .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_MESSAGE_NOT_FOUND));
        if (requestDto.action().equals(ActionType.ADD)) {
            channelMessage.addReaction(requestDto.emoji(), memberId);
        } else if (requestDto.action().equals(ActionType.REMOVE)) {
            channelMessage.removeReaction(requestDto.emoji(), memberId);
        }
        channelMessage.setEventType(EventType.REACT);
        channelMessage.setTargetEmoji(requestDto.emoji());
        channelMessageRepository.save(channelMessage);
        kafkaTemplate.send(KafkaConstants.CHANNEL_CHAT_EVENT_TOPIC, channelMessage);
    }

    public void replyToChannelMessage(ChannelMessageReplyRequestDto requestDto, Long memberId) {
        ChannelMessage parentChannelMessage = channelMessageRepository.findById(
                requestDto.parentId())
            .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_MESSAGE_NOT_FOUND));
        ChannelMessage channelMessage = requestDto.toEntity(memberId,
            parentChannelMessage.getContent());
        channelMessageRepository.save(channelMessage);
        kafkaTemplate.send(KafkaConstants.CHANNEL_CHAT_EVENT_TOPIC, channelMessage);
    }

    public void modifyChannelMessage(ChannelMessageModifyRequestDto requestDto, Long memberId) {
        ChannelMessage channelMessage = channelMessageRepository.findById(requestDto.id())
            .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_MESSAGE_NOT_FOUND));

        // TEXT 타입 메시지만 수정 가능
        if (!channelMessage.getType().equals(MessageType.TEXT)) {
            throw new CustomException(ErrorCode.INVALID_MESSAGE_TYPE_EXCEPTION);
        }
        // 메시지 전송자만 수정 가능
        if (!channelMessage.getMemberId().equals(memberId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        channelMessage.modifyContent(requestDto.content());
        channelMessage.setEventType(EventType.MODIFY);
        channelMessageRepository.save(channelMessage);
        kafkaTemplate.send(KafkaConstants.CHANNEL_CHAT_EVENT_TOPIC, channelMessage);
    }
}
