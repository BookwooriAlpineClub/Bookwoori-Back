package org.bookwoori.core.domain.record.dto.response;

import lombok.Builder;
import org.bookwoori.core.domain.book.dto.response.BookInfoDto;
import org.bookwoori.core.domain.record.entity.Record;

@Builder
public record RecordResponseDto(
        String isbn13,
        String title,
        String author,
        String cover,
        int itemPage,

        RecordInfoDto record
) {
    public static RecordResponseDto from(Record record) {
        return RecordResponseDto.builder()
                .isbn13(record.getBook().getIsbn13())
                .title(record.getBook().getTitle())
                .author(record.getBook().getAuthor())
                .cover(record.getBook().getCoverImg())
                .itemPage(record.getBook().getItemPage())
                .record(RecordInfoDto.from(record))
                .build();
    }
}

