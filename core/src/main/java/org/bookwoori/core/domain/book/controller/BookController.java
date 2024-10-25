package org.bookwoori.core.domain.book.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.book.dto.response.BookDetailResponseDto;
import org.bookwoori.core.domain.book.dto.response.BookResponseDto;
import org.bookwoori.core.domain.book.service.BookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Book")
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

  private final BookService bookService;

  @GetMapping
  public List<BookResponseDto> getBookList(@RequestParam String keyword) { // 도서 검색
    return bookService.findBooksByKeyword(keyword);
  }

  @GetMapping("/{isbn13}")
  public BookDetailResponseDto getBook(@PathVariable String isbn13) { // 상세정보 조회
    return bookService.findBookByIsbn(isbn13);
  }

}
