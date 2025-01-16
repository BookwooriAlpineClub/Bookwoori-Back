package org.bookwoori.core.domain.climbing.dto.response;

import lombok.Builder;
import org.bookwoori.core.domain.book.dto.response.BookInfoDto;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.review.entity.Review;

@Builder
public record ClimbingReviewWithShareResponseDto(
    boolean hasShared,
    boolean isShareable,
    BookInfoDto bookInfo,
    Long reviewId,
    int star,
    String content
) {

    public static ClimbingReviewWithShareResponseDto from(Climbing climbing, Review review) {
        return ClimbingReviewWithShareResponseDto.builder()
            .hasShared(false)
            .isShareable(true)
            .bookInfo(BookInfoDto.from(climbing.getBook()))
            .reviewId(review.getReviewId())
            .star(review.getRecord().getStarReview())
            .content(review.getContentReview())
            .build();
    }

}
