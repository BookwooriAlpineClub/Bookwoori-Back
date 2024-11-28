package org.bookwoori.chat.domain.directMessage.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import org.bookwoori.chat.domain.directMessage.entity.DirectMessage;

@Builder
public record RecentDirectMessageResponseDto(
    String content,
    LocalDateTime sendAt) {

    public static RecentDirectMessageResponseDto from(DirectMessage message) {
        return RecentDirectMessageResponseDto.builder()
            .content(message.getContent())
            .sendAt(message.getCreatedAt())
            .build();
    }
}
