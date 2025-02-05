package org.bookwoori.core.domain.record.dto.response;

import lombok.Builder;
import org.bookwoori.core.domain.book.dto.response.BookInfoDto;
import org.bookwoori.core.domain.record.entity.ReadingStatus;
import org.bookwoori.core.domain.record.entity.Record;

@Builder
public record RecordResponseDto(
    Long recordId,
    ReadingStatus readingStatus,
    int currentPage,
    int maxPage
) {

    public static RecordResponseDto from(Record record) {
        return RecordResponseDto.builder()
            .recordId(record.getRecordId())
            .readingStatus(record.getStatus())
            .currentPage(record.getCurrentPage())
            .maxPage(record.getMaxPage())
            .build();
    }

}
