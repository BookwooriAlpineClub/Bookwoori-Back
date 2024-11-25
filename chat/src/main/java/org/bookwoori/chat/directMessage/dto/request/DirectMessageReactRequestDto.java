package org.bookwoori.chat.directMessage.dto.request;

import org.bookwoori.chat.global.ActionType;
import org.bookwoori.chat.global.EmojiType;

public record DirectMessageReactRequestDto(
    String id,
    Long memberId,
    EmojiType emoji,
    ActionType action
) {

}
