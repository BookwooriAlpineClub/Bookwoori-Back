package org.bookwoori.core.domain.record.dto.response;

import java.time.LocalDate;
import lombok.Builder;
import org.bookwoori.core.domain.record.entity.Record;

@Builder
public record RecordListResponseDto(
  String isbn13,
  String title,
  String author,
  String cover,
  String publisher,
  LocalDate pubDate,
  String description,
  int itemPage,
  RecordResponseDto record
){

  public static RecordListResponseDto from(Record record) {
    return RecordListResponseDto.builder()
        .isbn13(record.getBook().getIsbn13())
        .title(record.getBook().getTitle())
        .author(record.getBook().getAuthor())
        .cover(record.getBook().getCoverImg())
        .publisher(record.getBook().getPublisher())
        .pubDate(record.getBook().getPubDate())
        .description(record.getBook().getDescription())
        .itemPage(record.getBook().getItemPage())
        .record(RecordResponseDto.from(record))
        .build();
  }
}
