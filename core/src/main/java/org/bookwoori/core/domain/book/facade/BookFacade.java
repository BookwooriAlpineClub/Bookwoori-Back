package org.bookwoori.core.domain.book.facade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bookwoori.core.domain.book.dto.response.BookDetailsResponseDto;
import org.bookwoori.core.domain.book.dto.response.BookResponseDto;
import org.bookwoori.core.domain.book.service.BookService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Log4j2
public class BookFacade {

    private final BookService bookService;

    @Transactional(readOnly = true)
    public List<BookResponseDto> getBooksByKeyword(String keyword) {
        return bookService.getBooksByKeyword(keyword);
    }

    @Transactional(readOnly = true)
    public BookDetailsResponseDto getBookByIsbn(String isbn13) {
        return bookService.getBookByIsbn(isbn13);
    }
}
