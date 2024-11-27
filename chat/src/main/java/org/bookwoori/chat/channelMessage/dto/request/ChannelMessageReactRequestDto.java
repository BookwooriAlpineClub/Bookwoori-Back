package org.bookwoori.chat.channelMessage.dto.request;

import org.bookwoori.chat.global.common.ActionType;
import org.bookwoori.chat.global.common.EmojiType;

public record ChannelMessageReactRequestDto(
    String id,
    EmojiType emoji,
    ActionType action
) {

}
