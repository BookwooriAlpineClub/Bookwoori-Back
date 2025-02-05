package org.bookwoori.core.domain.book.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record BookResponseDto(
    String title,
    String author,
    String publisher,
    LocalDate pubDate,
    String isbn13,
    String cover) {


    public static BookResponseDto from(JsonNode item) {
        return BookResponseDto.builder()
            .title(item.path("title").asText())
            .author(item.path("author").asText())
            .publisher(item.path("publisher").asText())
            .pubDate(LocalDate.parse(item.path("pubDate").asText().split("-")[0]))
            .isbn13(item.path("isbn13").asText())
            .cover(item.path("cover").asText())
            .build();
    }
}