package org.bookwoori.core.domain.book.dto.response;

import java.time.LocalDate;
import lombok.Builder;
import org.bookwoori.core.domain.book.entity.Book;

@Builder
public record BookInfoDto(
    String title,
    String author,
    String publisher,
    LocalDate pubDate,
    int itemPage,
    String description,
    String isbn13,
    String cover) {

    public static BookInfoDto from(Book book) {
        return BookInfoDto.builder()
            .title(book.getTitle())
            .author(book.getAuthor())
            .publisher(book.getPublisher())
            .pubDate(book.getPubDate())
            .itemPage(book.getItemPage())
            .description(book.getDescription())
            .isbn13(book.getIsbn13())
            .cover(book.getCoverImg())
            .build();
    }
}
