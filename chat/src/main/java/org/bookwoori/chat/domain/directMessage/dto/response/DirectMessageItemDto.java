package org.bookwoori.chat.domain.directMessage.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import org.bookwoori.chat.domain.directMessage.entity.DirectMessage;

@Builder
public record DirectMessageItemDto(
    String id,
    Long messageRoomId,
    Long memberId,
    String content,
    LocalDateTime createdAt
) {

    public static DirectMessageItemDto from(DirectMessage directMessage) {
        return DirectMessageItemDto.builder()
            .id(directMessage.getId())
            .messageRoomId(directMessage.getMessageRoomId())
            .memberId(directMessage.getMemberId())
            .content(directMessage.getContent())
            .createdAt(directMessage.getCreatedAt())
            .build();
    }
}
