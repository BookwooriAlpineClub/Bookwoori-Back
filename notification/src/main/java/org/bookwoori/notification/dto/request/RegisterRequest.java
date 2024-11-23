package org.bookwoori.notification.dto.request;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bookwoori.notification.domain.type.Platform;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotNull
    private Long userId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Platform platform;

    @NotNull
    private String token;
}
