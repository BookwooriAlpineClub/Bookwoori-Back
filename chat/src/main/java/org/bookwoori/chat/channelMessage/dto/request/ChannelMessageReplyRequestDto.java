package org.bookwoori.chat.channelMessage.dto.request;

import java.time.LocalDateTime;
import org.bookwoori.chat.channelMessage.domain.ChannelMessage;
import org.bookwoori.chat.global.common.EventType;
import org.bookwoori.chat.global.common.MessageType;

public record ChannelMessageReplyRequestDto(
    String parentId,
    Long channelId,
    MessageType type,
    String content
) {

    public ChannelMessage toEntity(Long memberId, String parentContent) {
        return ChannelMessage.builder()
            .parentId(this.parentId)
            .channelId(this.channelId)
            .memberId(memberId)
            .type(this.type)
            .content(this.content)
            .createdAt(LocalDateTime.now())
            .parentContent(parentContent)
            .eventType(EventType.REPLY)
            .build();
    }
}
