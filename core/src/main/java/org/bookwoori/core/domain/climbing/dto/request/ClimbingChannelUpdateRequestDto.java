package org.bookwoori.core.domain.climbing.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import org.bookwoori.core.domain.climbing.dto.validation.ValidDateRange;

@ValidDateRange
public record ClimbingChannelUpdateRequestDto(
    @NotBlank
    @Size(max = 40)
    String name,
    String description,
    @NotNull LocalDate startDate,
    @NotNull LocalDate endDate
) {

}
