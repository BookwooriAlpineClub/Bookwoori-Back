package org.bookwoori.core.domain.record.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.bookwoori.core.domain.book.entity.Book;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.record.entity.ReadingStatus;
import org.bookwoori.core.domain.record.entity.Record;
import org.bookwoori.core.domain.review.entity.Review;


public record RecordRequestDto(
    @NotBlank
    String isbn13,
    @NotNull
    ReadingStatus status,
    int starReview,
    LocalDate startDate,
    int currentPage,
    String contentReview

) {

    public Record toRecordEntity(Member currentMember, Book book) {
        return Record.builder()
            .member(currentMember)
            .book(book)
            .status(this.status)
            .starReview(this.starReview)
            .startDate(this.startDate)
            .currentPage(this.currentPage)
//            .review(this.review)
//            .isbn13(this.isbn13)
            .build();
    }

    public Review toReviewEntity(Record record, String contentReview) {
        return Review.builder()
            .record(record)
            .contentReview(contentReview)
            .build();
    }

}
