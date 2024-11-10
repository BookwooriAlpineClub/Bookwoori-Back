package org.bookwoori.core.domain.climbing.dto.response;

import java.util.List;
import org.bookwoori.core.domain.reviewEmoji.entity.EmojiType;

public record ReviewEmojiListDto(
    EmojiType emoji,
    List<ReviewEmojiMemberUnitDto> reviewEmojiMemberList
) {

}

