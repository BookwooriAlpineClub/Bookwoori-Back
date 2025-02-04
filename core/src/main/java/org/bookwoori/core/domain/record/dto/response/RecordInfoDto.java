package org.bookwoori.core.domain.record.dto.response;

import lombok.Builder;
import org.bookwoori.core.domain.record.entity.Record;

import java.time.LocalDate;

@Builder
public record RecordInfoDto(
        Long recordId,
        String status,
        LocalDate startDate,
        LocalDate endDate,
        int currentPage
) {
    public static RecordInfoDto from(Record record) {
        return RecordInfoDto.builder()
                .recordId(record.getRecordId())
                .status(record.getStatus().name())  // Enum을 String으로 변환
                .startDate(record.getStartDate())
                .endDate(record.getEndDate())
                .currentPage(record.getCurrentPage())
                .build();
    }
}
