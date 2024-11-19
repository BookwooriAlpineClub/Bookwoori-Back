package org.bookwoori.chat.directMessage.dto.response;

import java.util.List;

public record DirectMessageListResponseDto(
    List<DirectMessageItemDto> messages
) {

}
