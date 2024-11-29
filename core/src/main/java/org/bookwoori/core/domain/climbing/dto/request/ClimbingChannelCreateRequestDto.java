package org.bookwoori.core.domain.climbing.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import org.bookwoori.core.domain.book.entity.Book;
import org.bookwoori.core.domain.climbing.dto.validation.ValidDateRange;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.climbing.entity.ClimbingStatus;
import org.bookwoori.core.domain.server.entity.Server;

@ValidDateRange
public record ClimbingChannelCreateRequestDto(
    @NotNull Long serverId,
    @NotBlank
    @Size(max = 40, message = "INVALID_INPUT_LENGTH-채널명은 40자 이내여야 합니다.")
    String name,
    @NotBlank String isbn,
    String description,
    @NotNull LocalDate startDate,
    @NotNull LocalDate endDate
) {

    public Climbing toEntity(Server server, Book book, ClimbingStatus status) {
        return Climbing.builder()
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
