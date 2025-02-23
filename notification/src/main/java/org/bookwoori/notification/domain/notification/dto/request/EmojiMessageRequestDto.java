package org.bookwoori.notification.domain.notification.dto.request;

import jakarta.validation.constraints.NotNull;


public record EmojiMessageRequestDto(
        @NotNull
        Long memberId,

        @NotNull
        String nickname,

        @NotNull
        String type,

        @NotNull
        String content,

        @NotNull
        Long reviewId,

        @NotNull
        String target
) {


}
