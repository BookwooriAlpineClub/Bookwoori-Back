package org.bookwoori.chat.directMessage.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import org.bookwoori.chat.directMessage.domain.DirectMessage;

@Builder
public record DirectMessageItemDto(
    String id,
    Long messageRoomId,
    Long memberId,
    String nickname,
    String profileImg,
    String content,
    LocalDateTime createdAt
) {

    public static DirectMessageItemDto from(DirectMessage directMessage) {
        return DirectMessageItemDto.builder()
            .id(directMessage.getId())
            .messageRoomId(directMessage.getMessageRoomId())
            .memberId(directMessage.getMemberId())
            .nickname(directMessage.getNickname())
            .profileImg(directMessage.getProfileImg())
            .content(directMessage.getContent())
            .createdAt(directMessage.getCreatedAt())
            .build();
    }
}
