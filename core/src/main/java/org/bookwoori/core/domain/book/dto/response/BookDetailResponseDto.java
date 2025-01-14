package org.bookwoori.core.domain.book.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDate;
import lombok.Builder;
import org.bookwoori.core.domain.book.entity.Book;

@Builder
public record BookDetailResponseDto(
    String title,
    String author,
    String publisher,
    String pubYear,
    Long itemPage,
    String description,
    String isbn13,
    String cover) {

    public static BookDetailResponseDto from(JsonNode item) {
        return BookDetailResponseDto.builder()
            .title(item.path("title").asText())
            .author(item.path("author").asText())
            .publisher(item.path("publisher").asText())
            .pubYear(item.path("pubDate").asText().split("-")[0])
            .itemPage(item.path("subInfo").path("itemPage").asLong())
            .description(item.path("description").asText())
            .isbn13(item.path("isbn13").asText())
            .cover(item.path("cover").asText())
            .build();
    }

    public Book toEntity() {
        return Book.builder()
            .title(this.title)
            .author(this.author)
            .publisher(this.publisher)
            .pubYear(this.pubYear)
            .itemPage(this.itemPage != null ? Math.toIntExact(this.itemPage) : null)
            .isbn13(this.isbn13)
            .description(this.description)
            .cover(this.cover)
            .build();
    }
}



