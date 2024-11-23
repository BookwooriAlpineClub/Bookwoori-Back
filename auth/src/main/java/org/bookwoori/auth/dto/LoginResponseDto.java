package org.bookwoori.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginResponseDto(@NotBlank String accessToken) {

}
