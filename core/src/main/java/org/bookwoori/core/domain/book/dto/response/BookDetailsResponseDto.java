package org.bookwoori.core.domain.book.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDate;
import lombok.Builder;
import org.bookwoori.core.domain.book.entity.Book;

@Builder
public record BookDetailsResponseDto(
    String title,
    String author,
    String publisher,
    String pubDate,
    Long itemPage,
    String description,
    String isbn13,
    String cover) {

    public static BookDetailsResponseDto from(JsonNode item) {
        return BookDetailsResponseDto.builder()
            .title(item.path("title").asText())
            .author(item.path("author").asText())
            .publisher(item.path("publisher").asText())
            .pubDate(item.path("pubDate").asText())
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
            .pubDate(LocalDate.parse(this.pubDate))
            .itemPage(this.itemPage != null ? Math.toIntExact(this.itemPage) : null)
            .isbn13(this.isbn13)
            .description(this.description)
            .coverImg(this.cover)
            .build();
    }
}



