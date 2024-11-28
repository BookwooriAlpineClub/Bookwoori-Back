package org.bookwoori.chat.global.common.dto;

import java.util.Set;
import lombok.Builder;

@Builder
public record EmojiDetailsDto(
    int count,
    Set<Long> members
) {

    public static EmojiDetailsDto from(Set<Long> members) {
        return EmojiDetailsDto.builder()
            .count(members.size())
            .members(members)
            .build();
    }
}
