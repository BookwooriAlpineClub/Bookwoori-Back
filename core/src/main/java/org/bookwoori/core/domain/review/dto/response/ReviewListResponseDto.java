package org.bookwoori.core.domain.review.dto.response;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import org.bookwoori.core.domain.record.entity.Record;

@Builder
public record ReviewListResponseDto(
  String isbn13,
  String title,
  String author,
  String cover,
  String publisher,
  LocalDate pubDate,
  int itemPage,
  List<ReviewUnitDto> reviewList
){

public static ReviewListResponseDto from(Record record, List<ReviewUnitDto> reviewList) {
  return ReviewListResponseDto.builder()
      .isbn13(record.getBook().getIsbn13())
      .title(record.getBook().getTitle())
      .author(record.getBook().getAuthor())
      .cover(record.getBook().getCoverImg())
      .publisher(record.getBook().getPublisher())
      .pubDate(record.getBook().getPubDate())
      .itemPage(record.getBook().getItemPage())
      .reviewList(reviewList)
      .build();
}
}
