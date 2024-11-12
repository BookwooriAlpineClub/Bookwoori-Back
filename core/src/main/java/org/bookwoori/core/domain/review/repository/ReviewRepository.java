package org.bookwoori.core.domain.review.repository;

import java.util.List;
import org.bookwoori.core.domain.book.entity.Book;
import org.bookwoori.core.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.record.member.id IN :memberIds AND r.record.book = :book")
    List<Review> findByMemberIdsAndBook(@Param("memberIds") List<Long> memberIds,
        @Param("book") Book book);


    Review findByRecord_RecordId(Long recordId);

    void deleteByRecord_RecordId(Long recordId);

}
