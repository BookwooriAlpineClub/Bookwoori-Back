package org.bookwoori.core.domain.review.repository;

import java.util.Optional;
import org.bookwoori.core.domain.book.entity.Book;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByRecordMemberAndRecordBook(Member currentMember, Book book);
}
