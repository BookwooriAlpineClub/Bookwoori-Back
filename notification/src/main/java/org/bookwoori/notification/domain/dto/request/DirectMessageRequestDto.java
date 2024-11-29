package org.bookwoori.notification.domain.dto.request;

import jakarta.validation.constraints.NotNull;


public record DirectMessageRequestDto(
        @NotNull
        Long memberId,

        @NotNull
        String nickname,

        @NotNull
        String type,

        @NotNull
        String content,

        @NotNull
        String roomName,

        @NotNull
        Long roomId,

        @NotNull
        String target
) {


}
