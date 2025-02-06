package org.bookwoori.core.domain.climbing.dto.response;

import java.util.List;
import lombok.Builder;
import org.bookwoori.core.domain.book.dto.response.BookInfoDto;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.review.dto.response.ReviewUnitDto;
import org.bookwoori.core.domain.review.entity.Review;

@Builder
public record ClimbingReviewWithShareResponseDto(
    boolean hasShared,
    boolean isShareable,
    BookInfoDto bookInfo,
    List<ReviewUnitDto> reviewList
) {

    public static ClimbingReviewWithShareResponseDto from(Climbing climbing, List<ReviewUnitDto> reviewList) {
        return ClimbingReviewWithShareResponseDto.builder()
            .hasShared(false)
            .isShareable(true)
            .bookInfo(BookInfoDto.from(climbing.getBook()))
            .reviewList(reviewList)
            .build();
    }

}
