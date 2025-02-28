package org.bookwoori.core.domain.review.repository;

import java.util.List;
import java.util.Optional;
import org.bookwoori.core.domain.book.entity.Book;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.record.entity.Record;
import org.bookwoori.core.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.record.member.id IN :memberIds AND r.record.book = :book")
    List<Review> findByMemberIdsAndBook(@Param("memberIds") List<Long> memberIds,
        @Param("book") Book book);

    @Query("SELECT r FROM Review r WHERE r.record.member = :member AND r.record.book = :book")
    Review findByMemberAndBook(@Param("member") Member member, @Param("book") Book book);

    boolean existsByRecord_MemberAndRecord_Book(Member member, Book book);

    Optional<Review> findByRecord_RecordId(Long recordId);

    void deleteByRecord_RecordId(Long recordId);

    List<Review> findAllByRecord_RecordId(Long recordId);

    @Query("SELECT r FROM Review r WHERE r.record.member = :member AND r.record.book = :book")
    List<Review> findAllByMemberAndBook(@Param("member") Member member, @Param("book") Book book);

    @Query("SELECT AVG(r.star) FROM Review r WHERE r.record = :record")
    Double findAverageStarByRecord(@Param("record") Record record);
}
