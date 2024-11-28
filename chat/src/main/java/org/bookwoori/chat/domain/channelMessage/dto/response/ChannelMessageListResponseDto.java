package org.bookwoori.chat.domain.channelMessage.dto.response;

import java.util.List;

public record ChannelMessageListResponseDto(
    List<ChannelMessageItemDto> messages
) {

}
