package org.bookwoori.core.domain.member.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateMemberRequestDto(
    @NotNull String nickname,
    String profileImg) {
}
