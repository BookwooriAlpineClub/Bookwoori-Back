package org.bookwoori.core.domain.record.dto.response;

import lombok.Builder;
import org.bookwoori.core.domain.book.dto.response.BookInfoDto;
import org.bookwoori.core.domain.record.entity.ReadingStatus;
import org.bookwoori.core.domain.record.entity.Record;

@Builder
public record RecordResponseDto(
    Long recordId,
    Long memberId,
    ReadingStatus readingStatus,
    int star,
    int currentPage,
    int maxPage,
    BookInfoDto bookInfo

) {

    public static RecordResponseDto from(Record record) {
        return RecordResponseDto.builder()
            .recordId(record.getRecordId())
            .memberId(record.getMember().getMemberId())
            .readingStatus(record.getStatus())
            .star(record.getStarReview())
            .currentPage(record.getCurrentPage())
            .maxPage(record.getMaxPage())
            .bookInfo(BookInfoDto.from(record.getBook()))
            .build();
    }

}
