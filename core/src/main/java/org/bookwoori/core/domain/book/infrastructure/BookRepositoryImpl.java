package org.bookwoori.core.domain.book.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.book.repository.BookRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {

    private final BookJpaRepository bookJpaRepository;
}
