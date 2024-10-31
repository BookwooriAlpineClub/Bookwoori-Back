package org.bookwoori.core.domain.climbing.dto.response;

import java.time.LocalDate;
import org.bookwoori.core.domain.book.dto.response.BookInfoDto;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.climbing.entity.ClimbingStatus;

public record ClimbingDetailsResponseDto(
    Long climbingId,
    ClimbingStatus status,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    String description,
    int memberCount,
    boolean isJoined,
    boolean isOWner,
    BookInfoDto bookInfo) {

    public static ClimbingDetailsResponseDto from(Climbing climbing, int memberCount,
        boolean isJoined, boolean isOWner) {
        return new ClimbingDetailsResponseDto(
            climbing.getClimbingId(),
            climbing.getStatus(),
            climbing.getName(),
            climbing.getStartDate(),
            climbing.getEndDate(),
            climbing.getDescription(),
            memberCount,
            isJoined,
            isOWner,
            BookInfoDto.from(climbing.getBook())
        );
    }
}
