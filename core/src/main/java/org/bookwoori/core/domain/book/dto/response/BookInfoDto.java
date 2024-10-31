package org.bookwoori.core.domain.book.dto.response;

import java.time.LocalDate;
import org.bookwoori.core.domain.book.entity.Book;

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
        return new BookInfoDto(
            book.getTitle(),
            book.getAuthor(),
            book.getPublisher(),
            book.getPubDate(),
            book.getItemPage(),
            book.getDescription(),
            book.getIsbn13(),
            book.getCoverImg()
        );
    }
}
