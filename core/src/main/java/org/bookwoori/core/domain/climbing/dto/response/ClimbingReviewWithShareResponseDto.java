package org.bookwoori.core.domain.climbing.dto.response;

import lombok.Builder;
import org.bookwoori.core.domain.book.dto.response.BookInfoDto;
import org.bookwoori.core.domain.climbing.infrastructure.ClimbingEntity;
import org.bookwoori.core.domain.review.infrastructure.ReviewEntity;

@Builder
public record ClimbingReviewWithShareResponseDto(
    boolean hasShared,
    boolean isShareable,
    BookInfoDto bookInfo,
    Long reviewId,
    int star,
    String content
) {

    public static ClimbingReviewWithShareResponseDto from(ClimbingEntity climbing,
        ReviewEntity review) {
        return ClimbingReviewWithShareResponseDto.builder()
            .hasShared(false)
            .isShareable(true)
            .bookInfo(BookInfoDto.from(climbing.getBook()))
            .reviewId(review.getReviewId())
            .star(review.getRecord().getStar())
            .content(review.getContent())
            .build();
    }

}
