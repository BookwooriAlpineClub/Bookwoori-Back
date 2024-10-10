package org.bookwoori.core.domain.book.repository;

import org.bookwoori.core.domain.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}
