package org.bookwoori.chat.global.kafka;

import lombok.RequiredArgsConstructor;
import org.bookwoori.chat.channelMessage.domain.ChannelMessage;
import org.bookwoori.chat.channelMessage.dto.response.ChannelMessageReactResponseDto;
import org.bookwoori.chat.directMessage.domain.DirectMessage;
import org.bookwoori.chat.directMessage.dto.response.DirectMessageReactResponseDto;
import org.bookwoori.chat.directMessage.dto.response.DirectMessageReplyResponseDto;
import org.bookwoori.chat.directMessage.dto.response.DirectMessageSendResponseDto;
import org.bookwoori.chat.global.common.CommonResponse;
import org.bookwoori.chat.global.common.EventType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageListener { //토픽에 발행된 이벤트를 가져와 처리

    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = KafkaConstants.DIRECT_CHAT_TOPIC)
    public void directChatListener(DirectMessage directMessage) {
        CommonResponse response = CommonResponse.fromDirectMessage(EventType.NEW_MESSAGE,
            directMessage.getMessageRoomId(), DirectMessageSendResponseDto.from(directMessage));
        messagingTemplate.convertAndSend("/topic/direct/" + directMessage.getMessageRoomId(),
            response);
    }

    @KafkaListener(topics = KafkaConstants.CHANNEL_CHAT_TOPIC)
    public void channelChatListener(ChannelMessage message) {
        messagingTemplate.convertAndSend("/topic/channel/" + message.getChannelId(), message);
    }

    @KafkaListener(topics = KafkaConstants.DIRECT_CHAT_EVENT_TOPIC)
    public void directChatEventListener(DirectMessage directMessage) {

        CommonResponse response = null;

        switch (directMessage.getEventType()) {
            case REACT -> response = CommonResponse.fromDirectMessage(EventType.REACT,
                directMessage.getMessageRoomId(),
                DirectMessageReactResponseDto.from(directMessage));
            case REPLY -> response = CommonResponse.fromDirectMessage(EventType.REPLY,
                directMessage.getMessageRoomId(),
                DirectMessageReplyResponseDto.from(directMessage));
        }
        messagingTemplate.convertAndSend("/topic/direct/" + directMessage.getMessageRoomId(),
            response);
    }

    @KafkaListener(topics = KafkaConstants.CHANNEL_CHAT_EVENT_TOPIC)
    public void channelChatEventTopicListener(ChannelMessage channelMessage) {
        CommonResponse response = null;

        switch (channelMessage.getEventType()) {
            case REACT -> response = CommonResponse.fromChannelMessage(EventType.REACT,
                channelMessage.getChannelId(),
                ChannelMessageReactResponseDto.from(channelMessage));
        }
        messagingTemplate.convertAndSend("/topic/channel/" + channelMessage.getChannelId(),
            response);
    }

}

