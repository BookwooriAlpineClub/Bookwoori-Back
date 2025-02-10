package org.bookwoori.core.domain.record.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import org.bookwoori.core.domain.book.entity.Book;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.record.entity.ReadingStatus;
import org.bookwoori.core.domain.record.entity.Record;
import org.bookwoori.core.domain.review.dto.response.ReviewUnitDto;
import org.bookwoori.core.domain.review.entity.Review;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;


public record RecordRequestDto(
    @NotBlank
    @Size(min = 13, max = 13)
    String isbn13,
    @NotNull
    ReadingStatus status,
    LocalDate startDate,
    LocalDate endDate,
    int currentPage
) {

    public Record toRecordEntity(Member currentMember, Book book) {
        if (currentPage > book.getItemPage()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_PAGE);
        }
        return Record.builder()
            .member(currentMember)
            .book(book)
            .status(status)
            .startDate(startDate)
            .endDate(endDate)
            .currentPage(currentPage)
            .build();
    }

}
