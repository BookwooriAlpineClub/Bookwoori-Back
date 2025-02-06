package org.bookwoori.core.domain.record.dto.response;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import org.bookwoori.core.domain.book.dto.response.BookInfoDto;
import org.bookwoori.core.domain.record.entity.ReadingStatus;
import org.bookwoori.core.domain.record.entity.Record;
import org.bookwoori.core.domain.review.dto.response.ReviewUnitDto;
import org.bookwoori.core.domain.review.entity.Review;

@Builder
public record RecordDetailsResponseDto(
    String isbn13,
    String title,
    String author,
    String cover,
    String publisher,
    LocalDate pubDate,
    String description,
    int itemPage,
    RecordResponseDto record,
    List<ReviewUnitDto> reviewList
) {

    public static RecordDetailsResponseDto from(Record record, List<ReviewUnitDto> reviewList) {
        return RecordDetailsResponseDto.builder()
            .isbn13(record.getBook().getIsbn13())
            .title(record.getBook().getTitle())
            .author(record.getBook().getAuthor())
            .cover(record.getBook().getCoverImg())
            .publisher(record.getBook().getPublisher())
            .pubDate(record.getBook().getPubDate())
            .description(record.getBook().getDescription())
            .itemPage(record.getBook().getItemPage())
            .record(RecordResponseDto.from(record))
            .reviewList(reviewList)
            .build();
    }
}
