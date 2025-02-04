package org.bookwoori.chat.domain.channelMessage.dto.response;

import java.util.HashSet;
import java.util.Set;
import lombok.Builder;
import org.bookwoori.chat.domain.channelMessage.entity.ChannelMessage;
import org.bookwoori.chat.global.common.EmojiType;

@Builder
public record ChannelMessageReactResponseDto(
    String id,
    EmojiType emoji,
    int emojiCount,
    Set<Long> members
) {

    public static ChannelMessageReactResponseDto from(ChannelMessage channelMessage) {
        return ChannelMessageReactResponseDto.builder()
            .id(channelMessage.getId())
            .emoji(channelMessage.getTargetEmoji())
            .emojiCount(
                channelMessage.getReactions()
                    .getOrDefault(channelMessage.getTargetEmoji(), new HashSet<>()).size())
            .members(channelMessage.getReactions().get(channelMessage.getTargetEmoji()))
            .build();
    }
}
