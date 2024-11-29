package org.bookwoori.notification.dto.request;

import jakarta.validation.constraints.NotNull;


public record ChatMessageRequestDto(
        @NotNull
        Long memberId,

        @NotNull
        String nickname,

        @NotNull
        String type,

        @NotNull
        String content,

        @NotNull
        String channelName,

        @NotNull
        Long communityId,

        @NotNull
        Long channelId,

        @NotNull
        String target
) {


}

