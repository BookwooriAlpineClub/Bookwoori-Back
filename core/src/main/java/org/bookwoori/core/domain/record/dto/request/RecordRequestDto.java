package org.bookwoori.core.domain.record.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import org.bookwoori.core.domain.book.entity.Book;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.record.entity.ReadingStatus;
import org.bookwoori.core.domain.record.entity.Record;
import org.bookwoori.core.domain.review.entity.Review;


public record RecordRequestDto(
    @NotBlank
    @Size(min = 13, max = 13)
    String isbn13,
    @NotNull
    ReadingStatus status,
    int star,
    LocalDate startDate,
    int currentPage,
    String reviewContent

) {

    public Record toRecordEntity(Member currentMember, Book book) {
        return Record.builder()
            .member(currentMember)
            .book(book)
            .status(this.status)
            .star(this.star)
            .startDate(this.startDate)
            .currentPage(this.currentPage)
//            .review(this.review)
//            .isbn13(this.isbn13)
            .build();
    }

    public Review toReviewEntity(Record record, String content) {
        return Review.builder()
            .record(record)
            .content(content)
            .build();
    }

}
