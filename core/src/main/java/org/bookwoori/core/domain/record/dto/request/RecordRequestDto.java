package org.bookwoori.core.domain.record.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import org.bookwoori.core.domain.book.infrastructure.BookEntity;
import org.bookwoori.core.domain.member.infrastructure.MemberEntity;
import org.bookwoori.core.domain.record.entity.ReadingStatus;
import org.bookwoori.core.domain.record.infrastructure.RecordEntity;
import org.bookwoori.core.domain.review.infrastructure.ReviewEntity;


public record RecordRequestDto(
    @NotBlank
    @Size(min = 13, max = 13)
    String isbn13,
    @NotNull
    ReadingStatus status,
    int star,
    LocalDate startDate,
    LocalDate endDate,
    int currentPage,
    String reviewContent
) {

    public RecordEntity toRecordEntity(MemberEntity currentMember, BookEntity book) {
        return RecordEntity.builder()
            .member(currentMember)
            .book(book)
            .status(status)
            .star(star)
            .startDate(startDate)
            .endDate(endDate)
            .currentPage(currentPage)
            .build();
    }

    public ReviewEntity toReviewEntity(RecordEntity record, String content) {
        return ReviewEntity.builder()
            .record(record)
            .content(content)
            .build();
    }

}
