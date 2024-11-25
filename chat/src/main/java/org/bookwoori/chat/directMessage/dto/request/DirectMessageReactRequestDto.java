package org.bookwoori.chat.directMessage.dto.request;

import org.bookwoori.chat.global.common.ActionType;
import org.bookwoori.chat.global.common.EmojiType;

public record DirectMessageReactRequestDto(
    String id,
    Long memberId,
    EmojiType emoji,
    ActionType action
) {

}
