package org.bookwoori.core.domain.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.bookwoori.core.domain.record.entity.Record;
import org.bookwoori.core.domain.review.entity.Review;

public record ReviewRequestDto(
    Long recordId,
    @Min(0)
    @Max(5)
    int star,
    String content
) {

  public Review toEntity(Record record) {
    return Review.builder()
        .record(record)
        .star(star)
        .content(content)
        .build();
  }
}
