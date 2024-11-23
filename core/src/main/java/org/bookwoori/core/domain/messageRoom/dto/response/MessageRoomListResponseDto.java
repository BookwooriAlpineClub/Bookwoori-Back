package org.bookwoori.core.domain.messageRoom.dto.response;

import java.util.List;

public record MessageRoomListResponseDto(
    List<MessageRoomItemDto> messageRooms
) {

}
