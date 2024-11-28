package org.bookwoori.chat.domain.directMessage.dto.request;

import java.time.LocalDateTime;
import org.bookwoori.chat.domain.directMessage.entity.DirectMessage;
import org.bookwoori.chat.global.common.MessageType;

public record DirectMessageSendRequestDto(
    Long messageRoomId,
    MessageType type,
    String content
) {

    public DirectMessage toEntity(Long memberId) {
        return DirectMessage.builder()
            .messageRoomId(this.messageRoomId)
            .memberId(memberId)
            .type(this.type)
            .content(this.content)
            .createdAt(LocalDateTime.now())
            .build();
    }
}
