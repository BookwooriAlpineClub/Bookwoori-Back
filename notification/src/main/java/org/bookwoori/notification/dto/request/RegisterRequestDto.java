package org.bookwoori.notification.dto.request;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import org.bookwoori.notification.domain.type.Platform;


public record RegisterRequestDto(
        @NotNull
        Long userId,

        @NotNull
        @Enumerated(EnumType.STRING)
        Platform platform,

        @NotNull
        String token
) {

}
