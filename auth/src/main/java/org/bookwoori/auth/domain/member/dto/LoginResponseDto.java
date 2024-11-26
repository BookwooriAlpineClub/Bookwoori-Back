package org.bookwoori.auth.domain.member.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginResponseDto(@NotBlank String accessToken) {

}
