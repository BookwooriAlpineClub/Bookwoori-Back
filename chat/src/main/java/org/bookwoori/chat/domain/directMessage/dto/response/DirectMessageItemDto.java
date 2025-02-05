package org.bookwoori.chat.domain.directMessage.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;
import org.bookwoori.chat.domain.directMessage.entity.DirectMessage;
import org.bookwoori.chat.global.common.EmojiType;
import org.bookwoori.chat.global.common.dto.EmojiDetailsDto;

@Builder
public record DirectMessageItemDto(
    @JsonInclude(Include.NON_EMPTY)
    String parentId,
    @JsonInclude(Include.NON_EMPTY)
    Long parentMemberId,
    @JsonInclude(Include.NON_EMPTY)
    String parentContent,
    String id,
    Long messageRoomId,
    Long memberId,
    String content,
    LocalDateTime createdAt,
    Map<EmojiType, EmojiDetailsDto> reactions
) {

    public static DirectMessageItemDto from(DirectMessage directMessage) {
        return DirectMessageItemDto.builder()
            .parentId(directMessage.getParentId())
            .parentMemberId(directMessage.getParentMemberId())
            .parentContent(directMessage.getParentContent())
            .id(directMessage.getId())
            .messageRoomId(directMessage.getMessageRoomId())
            .memberId(directMessage.getMemberId())
            .content(directMessage.getContent())
            .createdAt(directMessage.getCreatedAt())
            .reactions(mapReactions(directMessage))
            .build();
    }

    private static Map<EmojiType, EmojiDetailsDto> mapReactions(DirectMessage directMessage) {
        return directMessage.getReactions().entrySet().stream().collect(
            Collectors.toMap(Map.Entry::getKey, entry -> EmojiDetailsDto.from(entry.getValue())));
    }
}
