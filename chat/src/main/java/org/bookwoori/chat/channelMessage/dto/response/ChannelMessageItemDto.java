package org.bookwoori.chat.channelMessage.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import org.bookwoori.chat.channelMessage.domain.ChannelMessage;

@Builder
public record ChannelMessageItemDto(
    String id,
    Long channelId,
    Long memberId,
    String content,
    LocalDateTime createdAt
) {

    public static ChannelMessageItemDto from(ChannelMessage message) {
        return ChannelMessageItemDto.builder()
            .id(message.getId())
            .channelId(message.getChannelId())
            .memberId(message.getMemberId())
            .content(message.getContent())
            .createdAt(message.getCreatedAt())
            .build();
    }
}
