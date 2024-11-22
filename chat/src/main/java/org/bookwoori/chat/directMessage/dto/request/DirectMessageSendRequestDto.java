package org.bookwoori.chat.directMessage.dto.request;

import java.time.LocalDateTime;
import org.bookwoori.chat.directMessage.domain.DirectMessage;
import org.bookwoori.chat.global.MessageType;

public record DirectMessageSendRequestDto(
    Long messageRoomId,
    Long memberId,
    MessageType type,
    String content
) {

    public DirectMessage toEntity(LocalDateTime now) {
        return DirectMessage.builder()
            .messageRoomId(this.messageRoomId)
            .memberId(this.memberId)
            .type(this.type)
            .content(this.content)
            .createdAt(now)
            .build();
    }
}
