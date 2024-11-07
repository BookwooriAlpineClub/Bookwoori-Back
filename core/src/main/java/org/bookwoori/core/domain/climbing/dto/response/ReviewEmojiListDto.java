package org.bookwoori.core.domain.climbing.dto.response;

import org.bookwoori.core.domain.reviewEmoji.entity.Emoji;

public record ReviewEmojiListDto(
    Emoji emoji,
    int emojiCount
) {

}
