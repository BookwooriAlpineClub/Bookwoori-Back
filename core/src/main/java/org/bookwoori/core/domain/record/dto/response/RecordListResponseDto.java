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
  int itemPage,
  RecordResponseDto record
){

  public static RecordListResponseDto from(Record record) {
    return RecordListResponseDto.builder()
        .isbn13(record.getBook().getIsbn13())
        .title(record.getBook().getTitle())
        .author(record.getBook().getAuthor())
        .cover(record.getBook().getCoverImg())
        .itemPage(record.getBook().getItemPage())
        .record(RecordResponseDto.from(record))
        .build();
  }
}
