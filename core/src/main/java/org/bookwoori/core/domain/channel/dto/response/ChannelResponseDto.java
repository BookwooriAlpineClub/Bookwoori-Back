package org.bookwoori.core.domain.channel.dto.response;

import lombok.Builder;
import org.bookwoori.core.domain.channel.entity.Channel;
import org.bookwoori.core.domain.channel.entity.ChannelType;

@Builder
public record ChannelResponseDto(
    Long channelId,
    String name,
    ChannelType type
) {

    public static ChannelResponseDto from(Channel channel) {
        return ChannelResponseDto.builder()
            .channelId(channel.getChannelId())
            .name(channel.getName())
            .type(channel.getChannelType())
            .build();
    }
}
