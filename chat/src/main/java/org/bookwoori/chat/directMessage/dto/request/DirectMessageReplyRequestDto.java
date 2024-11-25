package org.bookwoori.chat.directMessage.dto.request;

import java.time.LocalDateTime;
import org.bookwoori.chat.directMessage.domain.DirectMessage;
import org.bookwoori.chat.global.common.MessageType;

public record DirectMessageReplyRequestDto(
    String parentId,
    Long messageRoomId,
    Long memberId,
    MessageType type,
    String content
) {

    public DirectMessage toEntity() {
        return DirectMessage.builder()
            .parentId(this.parentId)
            .messageRoomId(this.messageRoomId)
            .memberId(this.memberId)
            .type(this.type)
            .content(this.content)
            .createdAt(LocalDateTime.now())
            .build();
    }
}
