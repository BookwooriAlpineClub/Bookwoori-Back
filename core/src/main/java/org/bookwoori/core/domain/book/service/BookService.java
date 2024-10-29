package org.bookwoori.core.domain.book.service;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.book.entity.Book;
import org.bookwoori.core.domain.book.repository.BookRepository;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Book getOrCreateBookByIsbn(String isbn) {
        return bookRepository.findByIsbn13(isbn)
            .orElseGet(() -> {
                Book newBook = Book.builder()
                    .title("테스트 제목")
                    .author("테스트 저자")
                    .publisher("테스트 출판사")
                    .pubDate(LocalDate.now())
                    .itemPage(1)
                    .isbn13(isbn)
                    .description("기본 설명")
                    .coverImg("default.jpg")
                    .build();
                return bookRepository.save(newBook);
            });
    }

    @Transactional(readOnly = true)
    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
            .orElseThrow(() -> new CustomException(ErrorCode.BOOK_NOT_FOUND));
    }

}
