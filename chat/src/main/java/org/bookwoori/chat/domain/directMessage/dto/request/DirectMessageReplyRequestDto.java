package org.bookwoori.chat.domain.directMessage.dto.request;

import java.time.LocalDateTime;
import org.bookwoori.chat.domain.directMessage.entity.DirectMessage;
import org.bookwoori.chat.global.common.EventType;
import org.bookwoori.chat.global.common.MessageType;

public record DirectMessageReplyRequestDto(
    String parentId,
    Long messageRoomId,
    MessageType type,
    String content
) {

    public DirectMessage toEntity(Long memberId, String parentContent) {
        return DirectMessage.builder()
            .parentId(this.parentId)
            .messageRoomId(this.messageRoomId)
            .memberId(memberId)
            .type(this.type)
            .content(this.content)
            .createdAt(LocalDateTime.now())
            .parentContent(parentContent)
            .eventType(EventType.REPLY)
            .build();
    }
}
