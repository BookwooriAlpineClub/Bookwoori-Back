package org.bookwoori.core.domain.book.repository;

import java.util.Optional;
import org.bookwoori.core.domain.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn13(String isbn);
}
