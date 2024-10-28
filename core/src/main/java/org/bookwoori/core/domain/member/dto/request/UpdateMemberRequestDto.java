package org.bookwoori.core.domain.member.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record UpdateMemberRequestDto(
        @Size(max= 10, message = "INVALID_INPUT_LENGTH-닉네임은 10자 이내여야 합니다.")
        @NotBlank String nickname,
        @Nullable MultipartFile profileImg) {
}
