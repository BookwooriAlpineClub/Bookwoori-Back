package org.bookwoori.notification.dto.request;

import jakarta.validation.constraints.NotNull;


public record DirectMessageRequestDto(
        @NotNull
        Long userId,

        @NotNull
        String username,

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
