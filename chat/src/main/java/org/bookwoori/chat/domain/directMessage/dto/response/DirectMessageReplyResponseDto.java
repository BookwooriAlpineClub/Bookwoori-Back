package org.bookwoori.chat.domain.directMessage.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import org.bookwoori.chat.domain.directMessage.entity.DirectMessage;
import org.bookwoori.chat.global.common.MessageType;

@Builder
public record DirectMessageReplyResponseDto(
    String id,
    Long messageRoomId,
    Long memberId,
    MessageType type,
    String content,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt,
    String parentId,
    Long parentMemberId,
    String parentContent
) {

    public static DirectMessageReplyResponseDto from(DirectMessage directMessage) {
        return DirectMessageReplyResponseDto.builder()
            .id(directMessage.getId())
            .messageRoomId(directMessage.getMessageRoomId())
            .memberId(directMessage.getMemberId())
            .type(directMessage.getType())
            .content(directMessage.getContent())
            .createdAt(directMessage.getCreatedAt())
            .parentId(directMessage.getParentId())
            .parentMemberId(directMessage.getParentMemberId())
            .parentContent(directMessage.getParentContent())
            .build();
    }
}
