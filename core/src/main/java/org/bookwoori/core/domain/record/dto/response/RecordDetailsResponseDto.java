package org.bookwoori.core.domain.record.dto.response;

import java.time.LocalDate;
import java.util.Optional;
import lombok.Builder;
import org.bookwoori.core.domain.book.dto.response.BookInfoDto;
import org.bookwoori.core.domain.record.entity.ReadingStatus;
import org.bookwoori.core.domain.record.infrastructure.RecordEntity;
import org.bookwoori.core.domain.review.infrastructure.ReviewEntity;

@Builder
public record RecordDetailsResponseDto(
    Long recordId,
    LocalDate startDate,
    ReadingStatus readingStatus,
    int star,
    int currentPage,
    int maxPage,
    BookInfoDto bookInfo,
    Optional<ReviewEntity> review
) {

    public static RecordDetailsResponseDto from(RecordEntity record,
        Optional<ReviewEntity> review) {
        return RecordDetailsResponseDto.builder()
            .recordId(record.getRecordId())
            .startDate(record.getStartDate())
            .readingStatus(record.getStatus())
            .star(record.getStar())
            .currentPage(record.getCurrentPage())
            .maxPage(record.getMaxPage())
            .bookInfo(BookInfoDto.from(record.getBook()))
            .review(review)
            .build();
    }
}
