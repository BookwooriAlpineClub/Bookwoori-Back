package org.bookwoori.chat.channelMessage.dto.request;

import java.time.LocalDateTime;
import org.bookwoori.chat.channelMessage.domain.ChannelMessage;
import org.bookwoori.chat.global.MessageType;

public record ChannelMessageSendRequestDto(
    Long channelId,
    Long memberId,
    MessageType type,
    String content
) {

    public ChannelMessage toEntity(LocalDateTime now) {
        return ChannelMessage.builder()
            .channelId(this.channelId)
            .memberId(this.memberId)
            .type(this.type)
            .content(this.content)
            .createdAt(now)
            .build();
    }
}
