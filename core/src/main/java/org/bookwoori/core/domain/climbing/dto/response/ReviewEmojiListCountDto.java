package org.bookwoori.core.domain.climbing.dto.response;

import org.bookwoori.core.domain.reviewEmoji.entity.EmojiType;

public record ReviewEmojiListCountDto(
    EmojiType emoji,
    int emojiCount
) {

}
