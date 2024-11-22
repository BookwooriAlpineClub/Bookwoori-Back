package org.bookwoori.core.domain.messageRoom.dto.response;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.Builder;
import org.bookwoori.core.domain.member.dto.response.MemberProfileResponseDto;
import org.bookwoori.core.domain.messageRoom.entity.MessageRoom;

@Builder
public record MessageRoomDetailsResponseDto(
    Long messageRoomId,
    String title,
    Map<Long, MemberProfileResponseDto> participants,
    LocalDateTime createdAt
) {

    public static MessageRoomDetailsResponseDto from(MessageRoom messageRoom, String title,
        Map<Long, MemberProfileResponseDto> participants) {
        return MessageRoomDetailsResponseDto.builder()
            .messageRoomId(messageRoom.getMessageRoomId())
            .title(title)
            .participants(participants)
            .createdAt(messageRoom.getCreatedAt())
            .build();
    }
}
