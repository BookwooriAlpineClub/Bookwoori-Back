package org.bookwoori.core.domain.climbing.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.bookwoori.core.domain.climbing.dto.validation.ValidDateRange;

import java.time.LocalDate;

@ValidDateRange
public record ClimbingChannelUpdateRequestDto(
        @NotBlank
        @Size(max= 40, message = "INVALID_INPUT_LENGTH-채널명은 40자 이내여야 합니다.")
        String name,
        @NotNull Long bookId,
        String description,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
){
}
