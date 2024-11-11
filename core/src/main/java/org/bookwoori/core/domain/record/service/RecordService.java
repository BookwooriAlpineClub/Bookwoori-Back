package org.bookwoori.core.domain.record.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingMember;
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


    @Transactional
    public void saveRecord(Record record) {
        recordRepository.save(record);
    }

    @Transactional
    public Record getRecordById(Long recordId) {
        return recordRepository.findById(recordId)
            .orElseThrow(() -> new CustomException(ErrorCode.RECORD_NOT_FOUND));
    }

    public void deleteRecord(Long recordId) {
        recordRepository.deleteById(recordId);
    }
}
