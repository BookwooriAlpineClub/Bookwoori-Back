package org.bookwoori.core.domain.book.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDate;
import lombok.Builder;
import org.bookwoori.core.domain.book.entity.Book;

@Builder
public record BookDetailsResponseDto(
    String isbn13,
    String title,
    String author,
    String cover,
    String publisher,
    LocalDate pubDate,
    String description,
    int itemPage) {


    public static BookDetailsResponseDto from(JsonNode item) {
        return BookDetailsResponseDto.builder()
            .title(item.path("title").asText())
            .author(item.path("author").asText())
            .publisher(item.path("publisher").asText())
            .pubDate(LocalDate.parse(item.path("pubDate").asText()))
            .itemPage((int) item.path("subInfo").path("itemPage").asLong())
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
            .pubDate(this.pubDate)
            .itemPage(this.itemPage)
            .isbn13(this.isbn13)
            .description(this.description)
            .coverImg(this.cover)
            .build();
    }
}



