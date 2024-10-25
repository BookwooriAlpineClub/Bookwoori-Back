package org.bookwoori.core.domain.book.dto.response;

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

}



