package org.bookwoori.core.domain.record.dto.response;

import lombok.Builder;
import org.bookwoori.core.domain.record.entity.Record;

@Builder
public record FinishedRecordListResponseDto(
  String isbn13,
  String title,
  String author,
  String cover,
  int itemPage,
  RecordResponseDto record,
  double reviewStarAve
){
    public static FinishedRecordListResponseDto from(Record record, double reviewStarAve) {
      return FinishedRecordListResponseDto.builder()
          .isbn13(record.getBook().getIsbn13())
          .title(record.getBook().getTitle())
          .author(record.getBook().getAuthor())
          .cover(record.getBook().getCoverImg())
          .itemPage(record.getBook().getItemPage())
          .record(RecordResponseDto.from(record))
          .reviewStarAve(reviewStarAve)
          .build();
    }
  }