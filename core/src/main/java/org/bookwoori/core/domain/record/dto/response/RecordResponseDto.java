package org.bookwoori.core.domain.record.dto.response;

import lombok.Builder;
import org.bookwoori.core.domain.book.dto.response.BookInfoDto;
import org.bookwoori.core.domain.record.entity.ReadingStatus;
import org.bookwoori.core.domain.record.infrastructure.RecordEntity;

@Builder
public record RecordResponseDto(
    Long recordId,
    Long memberId,
    ReadingStatus readingStatus,
    int star,
    int currentPage,
    int maxPage,
    String reviewContent,
    BookInfoDto bookInfo
) {

    public static RecordResponseDto from(RecordEntity record, String reviewContent) {
        return RecordResponseDto.builder()
            .recordId(record.getRecordId())
            .memberId(record.getMember().getMemberId())
            .readingStatus(record.getStatus())
            .star(record.getStar())
            .currentPage(record.getCurrentPage())
            .maxPage(record.getMaxPage())
            .reviewContent(reviewContent)
            .bookInfo(BookInfoDto.from(record.getBook()))
            .build();
    }

}
