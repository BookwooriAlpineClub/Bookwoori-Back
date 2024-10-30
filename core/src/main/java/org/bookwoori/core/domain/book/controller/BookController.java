package org.bookwoori.core.domain.book.controller;

import io.swagger.v3.oas.annotations.Operation;
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


    @Operation(summary = "도서 검색 목록 조회", description = "특정 키워드로 검색한 도서 목록을 조회합니다.")
    @GetMapping
    public List<BookResponseDto> getBookList(@RequestParam String keyword) { // 도서 검색
        return bookService.getBooksByKeyword(keyword);
    }


    @Operation(summary = "책 상세정보 조회", description = "isbn으로 책의 상세 정보를 조회합니다.")
    @GetMapping("/{isbn13}")
    public BookDetailResponseDto getBook(@PathVariable String isbn13) { // 상세정보 조회
        return bookService.getBookByIsbn(isbn13);
    }

}
