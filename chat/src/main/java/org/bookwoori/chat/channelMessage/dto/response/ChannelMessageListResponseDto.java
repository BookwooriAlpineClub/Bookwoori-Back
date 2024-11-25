package org.bookwoori.chat.channelMessage.dto.response;

import java.util.List;

public record ChannelMessageListResponseDto(
    List<ChannelMessageItemDto> messages
) {

}
