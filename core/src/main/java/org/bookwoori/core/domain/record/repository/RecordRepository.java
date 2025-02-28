package org.bookwoori.core.domain.record.repository;

import java.util.List;
import java.util.Optional;
import org.bookwoori.core.domain.book.entity.Book;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.record.entity.ReadingStatus;
import org.bookwoori.core.domain.record.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {

    Optional<Record> findByMemberAndBook(Member climbingMember, Book book);

    List<Record> findAllByStatusAndMember(ReadingStatus status, Member member);

    List<Record> findAllByMember(Member member);

    boolean existsByMemberAndBook(Member member, Book book);
}
