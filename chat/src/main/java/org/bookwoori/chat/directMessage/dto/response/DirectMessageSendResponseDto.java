package org.bookwoori.chat.directMessage.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import org.bookwoori.chat.directMessage.domain.DirectMessage;
import org.bookwoori.chat.global.common.MessageType;

@Builder
public record DirectMessageSendResponseDto(
    String id,
    Long messageRoomId,
    Long memberId,
    MessageType type,
    String content,
    LocalDateTime createdAt
) {

    public static DirectMessageSendResponseDto from(DirectMessage directMessage) {
        return DirectMessageSendResponseDto.builder()
            .id(directMessage.getId())
            .messageRoomId(directMessage.getMessageRoomId())
            .memberId(directMessage.getMemberId())
            .type(directMessage.getType())
            .content(directMessage.getContent())
            .createdAt(directMessage.getCreatedAt())
            .build();
    }
}
