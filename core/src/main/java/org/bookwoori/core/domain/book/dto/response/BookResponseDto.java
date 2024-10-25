package org.bookwoori.core.domain.book.dto.response;

import lombok.Builder;

@Builder
public record BookResponseDto(
    String title,
    String author,
    String publisher,
    String pubYear,
    String isbn13,
    String cover) {

}

