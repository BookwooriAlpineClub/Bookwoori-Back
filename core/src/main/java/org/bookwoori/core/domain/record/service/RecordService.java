package org.bookwoori.core.domain.record.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.book.entity.Book;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingMember;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.record.entity.ReadingStatus;
import org.bookwoori.core.domain.record.entity.Record;
import org.bookwoori.core.domain.record.repository.RecordRepository;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;

    @Transactional(readOnly = true)
    public Record getMemberRecord(Member member, Book book) {
        return recordRepository.findByMemberAndBook(member, book)
            .orElseThrow(() -> new CustomException(ErrorCode.RECORD_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Optional<Record> getClimbingMemberRecordOpt(ClimbingMember climbingMember, Book book) {
        return recordRepository.findByMemberAndBook(climbingMember.getMember(), book);
    }

    @Transactional(readOnly = true)
    public void isFinished(Member member, Climbing climbing) {
        Record record = getMemberRecord(member, climbing.getBook());
        if (record.getStatus() != ReadingStatus.FINISHED) {
            throw new CustomException(ErrorCode.RECORD_NOT_FINISHED);
        }
    }
}
