package org.bookwoori.core.domain.messageRoom.dto.response;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.Builder;
import org.bookwoori.core.domain.member.dto.response.MemberProfileResponseDto;
import org.bookwoori.core.domain.messageRoom.infrastructure.MessageRoomEntity;

@Builder
public record MessageRoomDetailsResponseDto(
    Long messageRoomId,
    String title,
    Map<Long, MemberProfileResponseDto> participants,
    boolean isActive,
    LocalDateTime createdAt
) {

    public static MessageRoomDetailsResponseDto from(MessageRoomEntity messageRoom, String title,
        Map<Long, MemberProfileResponseDto> participants, boolean isActive) {
        return MessageRoomDetailsResponseDto.builder()
            .messageRoomId(messageRoom.getMessageRoomId())
            .title(title)
            .participants(participants)
            .isActive(isActive)
            .createdAt(messageRoom.getCreatedAt())
            .build();
    }
}
