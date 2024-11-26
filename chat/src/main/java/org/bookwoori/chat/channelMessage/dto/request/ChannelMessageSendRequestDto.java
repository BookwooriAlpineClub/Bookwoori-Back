package org.bookwoori.chat.channelMessage.dto.request;

import java.time.LocalDateTime;
import org.bookwoori.chat.channelMessage.domain.ChannelMessage;
import org.bookwoori.chat.global.common.MessageType;

public record ChannelMessageSendRequestDto(
    Long channelId,
    MessageType type,
    String content
) {

    public ChannelMessage toEntity(Long memberId) {
        return ChannelMessage.builder()
            .channelId(this.channelId)
            .memberId(memberId)
            .type(this.type)
            .content(this.content)
            .createdAt(LocalDateTime.now())
            .build();
    }
}
