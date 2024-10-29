package org.bookwoori.core.domain.book.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;

@Builder
public record BookDetailResponseDto(
    String title,
    String author,
    String publisher,
    String pubDate,
    Long itemPage,
    String description,
    String isbn13,
    String cover) {

    public static BookDetailResponseDto from(JsonNode item) {
        return BookDetailResponseDto.builder()
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

}



