package org.bookwoori.core.domain.book.infrastructure;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookJpaRepository extends JpaRepository<BookEntity, Long> {

    Optional<BookEntity> findByIsbn13(String isbn);
}
