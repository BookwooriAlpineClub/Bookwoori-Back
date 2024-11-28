package org.bookwoori.chat.domain.directMessage.dto.request;

import org.bookwoori.chat.global.common.ActionType;
import org.bookwoori.chat.global.common.EmojiType;

public record DirectMessageReactRequestDto(
    String id,
    EmojiType emoji,
    ActionType action
) {

}
