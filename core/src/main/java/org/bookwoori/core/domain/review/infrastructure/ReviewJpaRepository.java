package org.bookwoori.core.domain.review.infrastructure;

import java.util.List;
import java.util.Optional;
import org.bookwoori.core.domain.book.infrastructure.BookEntity;
import org.bookwoori.core.domain.member.infrastructure.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewJpaRepository extends JpaRepository<ReviewEntity, Long> {

    @Query("SELECT r FROM ReviewEntity r WHERE r.record.member.id IN :memberIds AND r.record.book = :book")
    List<ReviewEntity> findByMemberIdsAndBook(@Param("memberIds") List<Long> memberIds,
        @Param("book") BookEntity book);

    @Query("SELECT r FROM ReviewEntity r WHERE r.record.member = :member AND r.record.book = :book")
    ReviewEntity findByMemberAndBook(@Param("member") MemberEntity member,
        @Param("book") BookEntity book);

    boolean existsByRecord_MemberAndRecord_Book(MemberEntity member, BookEntity book);

    Optional<ReviewEntity> findByRecord_RecordId(Long recordId);

    void deleteByRecord_RecordId(Long recordId);

}
