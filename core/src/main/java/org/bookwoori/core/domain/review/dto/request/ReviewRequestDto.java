package org.bookwoori.core.domain.review.dto.request;

import org.bookwoori.core.domain.record.entity.Record;
import org.bookwoori.core.domain.review.entity.Review;

public record ReviewRequestDto(
    Long recordId,
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
