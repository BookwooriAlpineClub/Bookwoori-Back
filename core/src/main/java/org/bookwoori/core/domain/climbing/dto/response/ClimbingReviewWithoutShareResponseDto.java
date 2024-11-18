package org.bookwoori.core.domain.climbing.dto.response;

import lombok.Builder;
import org.bookwoori.core.domain.book.dto.response.BookInfoDto;
import org.bookwoori.core.domain.climbing.entity.Climbing;

@Builder
public record ClimbingReviewWithoutShareResponseDto(
    boolean hasShared,
    boolean isShareable,
    BookInfoDto bookInfo
) {

    public static ClimbingReviewWithoutShareResponseDto from(Climbing climbing) {
        return ClimbingReviewWithoutShareResponseDto.builder()
            .hasShared(false)
            .isShareable(false)
            .bookInfo(BookInfoDto.from(climbing.getBook()))
            .build();
    }
}
