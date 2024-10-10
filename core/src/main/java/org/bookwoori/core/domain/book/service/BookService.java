package org.bookwoori.core.domain.book.service;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.book.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
}
