package org.bookwoori.chat.directMessage.dto.request;

import java.time.LocalDateTime;
import org.bookwoori.chat.directMessage.domain.DirectMessage;
import org.bookwoori.chat.global.MessageType;

public record DirectMessageSendRequestDto(
    Long messageRoomId,
    Long memberId,
    String nickname,
    String profileImg,
    MessageType type,
    String content
) {

    public DirectMessage toEntity(LocalDateTime now) {
        return DirectMessage.builder()
            .messageRoomId(this.messageRoomId)
            .memberId(this.memberId)
            .nickname(this.nickname)
            .profileImg(this.profileImg)
            .type(this.type)
            .content(this.content)
            .createdAt(now)
            .build();
    }
}
