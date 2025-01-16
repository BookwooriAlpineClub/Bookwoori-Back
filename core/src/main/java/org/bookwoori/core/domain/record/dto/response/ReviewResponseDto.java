package org.bookwoori.core.domain.record.dto.response;

import lombok.Builder;
import org.bookwoori.core.domain.book.dto.response.BookInfoDto;
import org.bookwoori.core.domain.record.entity.Record;
import org.bookwoori.core.domain.review.entity.Review;

@Builder
public record ReviewResponseDto(
    Long recordId,
    Long memberId,
    int star,
    String reviewContent,
    BookInfoDto bookInfo

) {

    public static ReviewResponseDto from(Record record, Review review) {
        return ReviewResponseDto.builder()
            .recordId(record.getRecordId())
            .memberId(record.getMember().getMemberId())
            .star(record.getStarReview())
            .reviewContent(review.getContentReview())
            .bookInfo(BookInfoDto.from(record.getBook()))
            .build();
    }

}
