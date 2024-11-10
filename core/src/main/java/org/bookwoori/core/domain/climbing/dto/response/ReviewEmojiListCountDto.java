package org.bookwoori.core.domain.climbing.dto.response;

import org.bookwoori.core.domain.reviewEmoji.entity.Emoji;

public record ReviewEmojiListCountDto(
    Emoji emoji,
    int emojiCount
) {

}
