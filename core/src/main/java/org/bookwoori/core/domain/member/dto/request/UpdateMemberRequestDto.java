package org.bookwoori.core.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record UpdateMemberRequestDto(
    @Size(max = 10)
    @NotBlank String nickname) {
}
