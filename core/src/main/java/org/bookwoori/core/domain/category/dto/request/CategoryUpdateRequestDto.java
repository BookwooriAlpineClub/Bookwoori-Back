package org.bookwoori.core.domain.category.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryUpdateRequestDto(
    @NotBlank
    String name
) {

}
