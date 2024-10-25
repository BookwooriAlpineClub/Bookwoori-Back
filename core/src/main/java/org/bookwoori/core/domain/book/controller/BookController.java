package org.bookwoori.core.domain.book.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.book.dto.response.BookResponseDto;
import org.bookwoori.core.domain.book.service.BookService;
import org.springframework.web.bind.annotation.GetMapping;
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
    List<BookResponseDto> bookList = bookService.findBookByKeyword(keyword);
    return bookList;
  }

}
