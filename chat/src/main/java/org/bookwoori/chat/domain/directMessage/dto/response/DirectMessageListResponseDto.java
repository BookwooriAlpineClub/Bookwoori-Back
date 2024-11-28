package org.bookwoori.chat.domain.directMessage.dto.response;

import java.util.List;

public record DirectMessageListResponseDto(
    List<DirectMessageItemDto> messages
) {

}
