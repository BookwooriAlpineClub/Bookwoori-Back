package org.bookwoori.core.domain.member.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record UpdateMemberRequestDto(
        @NotNull String nickname,
        @Nullable MultipartFile profileImg) {
}
