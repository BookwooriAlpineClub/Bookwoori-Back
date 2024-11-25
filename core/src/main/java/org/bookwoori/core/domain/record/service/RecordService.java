package org.bookwoori.core.domain.record.service;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.book.entity.Book;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingMember;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.record.entity.ReadingStatus;
import org.bookwoori.core.domain.record.entity.Record;
import org.bookwoori.core.domain.record.repository.RecordRepository;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


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
    public Record saveRecord(Record record) {
        return recordRepository.save(record);
    }

    @Transactional
    public Record getRecordById(Long recordId) {
        return recordRepository.findById(recordId)
                .orElseThrow(() -> new CustomException(ErrorCode.RECORD_NOT_FOUND));
    }

    @Transactional
    public Record getRecordByMemberAndBook(Member member, Book book) {
        return recordRepository.findByMemberAndBook(member, book)
                .orElseThrow(() -> new CustomException(ErrorCode.RECORD_NOT_FOUND));
    }

    @Transactional
    public void deleteRecord(Long recordId) {
        recordRepository.deleteById(recordId);
    }

    @Transactional
    public List<Record> getRecordsByStatus(ReadingStatus status) {
        return recordRepository.findAllByStatus(status);
    }

    @Transactional
    public List<Record> getRecordsByMember(Member member) {
        return recordRepository.findAllByMember(member);
    }


}
