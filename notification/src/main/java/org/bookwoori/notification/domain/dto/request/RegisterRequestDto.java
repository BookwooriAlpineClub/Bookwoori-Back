package org.bookwoori.notification.domain.dto.request;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import org.bookwoori.notification.domain.type.Platform;


public record RegisterRequestDto(
        @NotNull
        Long memberId,

        @NotNull
        @Enumerated(EnumType.STRING)
        Platform platform,

        @NotNull
        String token
) {

}
