package org.bookwoori.core.domain.climbing.dto.response;

import java.time.LocalDate;
import lombok.Builder;
import org.bookwoori.core.domain.book.dto.response.BookInfoDto;
import org.bookwoori.core.domain.climbing.entity.ClimbingStatus;
import org.bookwoori.core.domain.climbing.infrastructure.ClimbingEntity;

@Builder
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

    public static ClimbingDetailsResponseDto from(ClimbingEntity climbing, int memberCount,
        boolean isJoined, boolean isOWner) {
        return ClimbingDetailsResponseDto.builder()
            .climbingId(climbing.getClimbingId())
            .status(climbing.getStatus())
            .name(climbing.getName())
            .startDate(climbing.getStartDate())
            .endDate(climbing.getEndDate())
            .description(climbing.getDescription())
            .memberCount(memberCount)
            .isJoined(isJoined)
            .isOWner(isOWner)
            .bookInfo(BookInfoDto.from(climbing.getBook()))
            .build();
    }
}
