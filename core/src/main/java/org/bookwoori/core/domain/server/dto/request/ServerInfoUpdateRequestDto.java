package org.bookwoori.core.domain.server.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ServerInfoUpdateRequestDto(
    @Size(max = 20)
    @NotBlank
    String name,
    String description
) {

}
