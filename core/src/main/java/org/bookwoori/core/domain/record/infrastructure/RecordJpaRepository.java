package org.bookwoori.core.domain.record.infrastructure;

import java.util.List;
import java.util.Optional;
import org.bookwoori.core.domain.book.infrastructure.BookEntity;
import org.bookwoori.core.domain.member.infrastructure.MemberEntity;
import org.bookwoori.core.domain.record.entity.ReadingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordJpaRepository extends JpaRepository<RecordEntity, Long> {

    Optional<RecordEntity> findByMemberAndBook(MemberEntity climbingMember, BookEntity book);

    List<RecordEntity> findAllByStatus(ReadingStatus status);

    List<RecordEntity> findAllByMember(MemberEntity member);

    boolean existsByMemberAndBook(MemberEntity member, BookEntity book);
}
