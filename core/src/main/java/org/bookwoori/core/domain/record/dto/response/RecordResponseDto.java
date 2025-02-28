package org.bookwoori.core.domain.record.dto.response;

import java.time.LocalDate;
import lombok.Builder;
import org.bookwoori.core.domain.book.dto.response.BookInfoDto;
import org.bookwoori.core.domain.record.entity.ReadingStatus;
import org.bookwoori.core.domain.record.entity.Record;
import org.springframework.cglib.core.Local;

@Builder
public record RecordResponseDto(
    Long recordId,
    ReadingStatus status,
    LocalDate startDate,
    LocalDate endDate,
    int currentPage
) {

    public static RecordResponseDto from(Record record) {
        return RecordResponseDto.builder()
            .recordId(record.getRecordId())
            .status(record.getStatus())
            .startDate(record.getStartDate())
            .endDate(record.getEndDate())
            .currentPage(record.getCurrentPage())
            .build();
    }

}
