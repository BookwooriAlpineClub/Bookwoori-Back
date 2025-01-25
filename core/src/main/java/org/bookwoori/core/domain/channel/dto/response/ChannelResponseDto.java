package org.bookwoori.core.domain.channel.dto.response;

import lombok.Builder;
import org.bookwoori.core.domain.channel.infrastructure.ChannelEntity;
import org.bookwoori.core.domain.channel.entity.ChannelType;

@Builder
public record ChannelResponseDto(
    Long channelId,
    String name,
    ChannelType type
) {

    public static ChannelResponseDto from(ChannelEntity channel) {
        return ChannelResponseDto.builder()
            .channelId(channel.getChannelId())
            .name(channel.getName())
            .type(channel.getChannelType())
            .build();
    }
}
