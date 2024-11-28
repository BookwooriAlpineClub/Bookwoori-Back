package org.bookwoori.chat.domain.directMessage.dto.response;

import java.util.HashSet;
import java.util.Set;
import lombok.Builder;
import org.bookwoori.chat.domain.directMessage.entity.DirectMessage;
import org.bookwoori.chat.global.common.EmojiType;

@Builder
public record DirectMessageReactResponseDto(
    String id,
    EmojiType emoji,
    int emojiCount,
    Set<Long> members
) {

    public static DirectMessageReactResponseDto from(DirectMessage directMessage) {
        return DirectMessageReactResponseDto.builder()
            .id(directMessage.getId())
            .emoji(directMessage.getTargetEmoji())
            .emojiCount(
                directMessage.getReactions()
                    .getOrDefault(directMessage.getTargetEmoji(), new HashSet<>()).size())
            .members(directMessage.getReactions().get(directMessage.getTargetEmoji()))
            .build();
    }
}
