package org.bookwoori.core.domain.climbing.dto.request;

import jakarta.validation.constraints.Size;

public record ClimbingMemoUpdateRequestDto(
    @Size(max = 10)
    String memo) {

}
