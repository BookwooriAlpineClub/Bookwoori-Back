package org.bookwoori.core.domain.category.dto.request;

import jakarta.validation.constraints.NotNull;

public record CategoryLocateRequestDto(
    @NotNull
    Long beforeCategoryId
) {

}
