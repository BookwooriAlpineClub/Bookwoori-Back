package org.bookwoori.chat.directMessage.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import org.bookwoori.chat.directMessage.domain.DirectMessage;

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
