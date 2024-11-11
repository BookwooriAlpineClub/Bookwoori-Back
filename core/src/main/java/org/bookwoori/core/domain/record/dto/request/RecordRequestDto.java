package org.bookwoori.core.domain.record.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.record.entity.ReadingStatus;
import org.bookwoori.core.domain.record.entity.Record;


public record RecordRequestDto(
    @NotBlank
    String isbn13,
    @NotBlank
    ReadingStatus status,
    Integer star,
    LocalDate startDate,
    Integer currentPage,
    String review

) {

    public Record toEntity(Member currentMember) {
        return Record.builder()
            .member(currentMember)
            .status(this.status)
            .star(this.star)
            .startDate(this.startDate)
            .currentPage(this.currentPage)
            .review(this.review)
            .isbn13(this.isbn13)
            .build();
    }
}
