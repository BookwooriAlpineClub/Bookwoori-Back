package org.bookwoori.core.domain.record.dto.response;

import lombok.Builder;
import org.bookwoori.core.domain.book.dto.response.BookInfoDto;
import org.bookwoori.core.domain.record.infrastructure.RecordEntity;
import org.bookwoori.core.domain.review.infrastructure.ReviewEntity;

@Builder
public record ReviewResponseDto(
    Long recordId,
    Long memberId,
    int star,
    String reviewContent,
    BookInfoDto bookInfo

) {

    public static ReviewResponseDto from(RecordEntity record, ReviewEntity review) {
        return ReviewResponseDto.builder()
            .recordId(record.getRecordId())
            .memberId(record.getMember().getMemberId())
            .star(record.getStar())
            .reviewContent(review.getContent())
            .bookInfo(BookInfoDto.from(record.getBook()))
            .build();
    }

}
