package org.bookwoori.core.domain.messageRoom.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import org.bookwoori.core.domain.messageRoom.entity.MessageRoom;

@Builder
public record MessageRoomInfoResponseDto(
    Long messageRoomId,
    LocalDateTime createdAt
) {

    public static MessageRoomInfoResponseDto from(MessageRoom messageRoom) {
        return MessageRoomInfoResponseDto.builder()
            .messageRoomId(messageRoom.getMessageRoomId())
            .createdAt(messageRoom.getCreatedAt())
            .build();
    }
}
