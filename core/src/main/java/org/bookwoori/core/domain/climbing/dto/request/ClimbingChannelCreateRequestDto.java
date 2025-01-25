package org.bookwoori.core.domain.climbing.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import org.bookwoori.core.domain.book.infrastructure.BookEntity;
import org.bookwoori.core.domain.climbing.dto.validation.ValidDateRange;
import org.bookwoori.core.domain.climbing.entity.ClimbingStatus;
import org.bookwoori.core.domain.climbing.infrastructure.ClimbingEntity;
import org.bookwoori.core.domain.server.infrastructure.ServerEntity;

@ValidDateRange
public record ClimbingChannelCreateRequestDto(
    @NotNull Long serverId,
    @NotBlank
    @Size(max = 40)
    String name,
    @NotBlank String isbn,
    String description,
    @NotNull LocalDate startDate,
    @NotNull LocalDate endDate
) {

    public ClimbingEntity toEntity(ServerEntity server, BookEntity book, ClimbingStatus status) {
        return ClimbingEntity.builder()
            .server(server)
            .book(book)
            .status(status)
            .name(name)
            .description(description)
            .startDate(startDate)
            .endDate(endDate)
            .build();
    }
}
