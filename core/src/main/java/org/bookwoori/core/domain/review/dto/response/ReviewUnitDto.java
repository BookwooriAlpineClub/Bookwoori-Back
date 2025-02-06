package org.bookwoori.core.domain.review.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import org.bookwoori.core.domain.review.entity.Review;

@Builder
public record ReviewUnitDto(
    Long reviewId,
    int star,
    String content,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt
) {

  public static ReviewUnitDto from(Review review){
    return ReviewUnitDto.builder()
        .reviewId(review.getReviewId())
        .star(review.getStar())
        .content(review.getContent())
        .createdAt(review.getCreatedAt())
        .modifiedAt(review.getModifiedAt())
        .build();
  }

}
