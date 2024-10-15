package org.bookwoori.core.domain.member.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginResponse(@NotBlank String accessToken) {
}
