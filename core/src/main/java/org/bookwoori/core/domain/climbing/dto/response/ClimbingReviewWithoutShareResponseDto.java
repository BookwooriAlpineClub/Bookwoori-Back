package org.bookwoori.core.domain.climbing.dto.response;

import lombok.Builder;
import org.bookwoori.core.domain.book.dto.response.BookInfoDto;
import org.bookwoori.core.domain.climbing.infrastructure.ClimbingEntity;

@Builder
public record ClimbingReviewWithoutShareResponseDto(
    boolean hasShared,
    boolean isShareable,
    BookInfoDto bookInfo
) {

    public static ClimbingReviewWithoutShareResponseDto from(ClimbingEntity climbing) {
        return ClimbingReviewWithoutShareResponseDto.builder()
            .hasShared(false)
            .isShareable(false)
            .bookInfo(BookInfoDto.from(climbing.getBook()))
            .build();
    }
}
