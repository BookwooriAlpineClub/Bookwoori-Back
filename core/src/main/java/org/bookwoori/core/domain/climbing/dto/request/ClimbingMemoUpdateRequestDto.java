package org.bookwoori.core.domain.climbing.dto.request;

import jakarta.validation.constraints.Size;

public record ClimbingMemoUpdateRequestDto(
    @Size(max = 10, message = "INVALID_INPUT_LENGTH-메모는 10자 이내여야 합니다.")
    String memo) {

}
