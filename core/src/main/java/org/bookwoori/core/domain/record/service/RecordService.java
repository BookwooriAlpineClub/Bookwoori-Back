package org.bookwoori.core.domain.record.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.book.entity.Book;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingMember;
import org.bookwoori.core.domain.record.entity.Record;
import org.bookwoori.core.domain.record.repository.RecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;

    @Transactional(readOnly = true)
    public Optional<Record> getClimbingMemberRecord(ClimbingMember climbingMember, Book book) {
        return recordRepository.findByMemberAndBook(climbingMember.getMember(), book);
    }
}
