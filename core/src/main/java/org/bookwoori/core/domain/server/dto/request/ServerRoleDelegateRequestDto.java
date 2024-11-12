package org.bookwoori.core.domain.server.dto.request;

import jakarta.validation.constraints.NotNull;

public record ServerRoleDelegateRequestDto(
    @NotNull
    Long memberId
) {

}
