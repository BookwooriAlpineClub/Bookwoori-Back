package org.bookwoori.core.domain.record.dto.response;

import lombok.Builder;
import org.bookwoori.core.domain.book.dto.response.BookInfoDto;
import org.bookwoori.core.domain.record.entity.Record;

@Builder
public record ReviewResponseDto(
    Long recordId,
    Long memberId,
    int star,
    String review,
    BookInfoDto bookInfo

) {

    public static ReviewResponseDto from(Record record) {
        return ReviewResponseDto.builder()
            .recordId(record.getRecordId())
            .memberId(record.getMember().getMemberId())
            .star(record.getStar())
            .review(record.getReview())
            .bookInfo(BookInfoDto.from(record.getBook()))
            .build();
    }

}
