package org.bookwoori.core.domain.record.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.book.infrastructure.BookEntity;
import org.bookwoori.core.domain.climbingMember.infrastructure.ClimbingMemberEntity;
import org.bookwoori.core.domain.member.infrastructure.MemberEntity;
import org.bookwoori.core.domain.record.entity.ReadingStatus;
import org.bookwoori.core.domain.record.infrastructure.RecordEntity;
import org.bookwoori.core.domain.record.infrastructure.RecordJpaRepository;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class RecordServiceImpl {

    private final RecordJpaRepository recordRepository;

    @Transactional(readOnly = true)
    public RecordEntity getMemberRecord(MemberEntity member, BookEntity book) {
        return recordRepository.findByMemberAndBook(member, book)
            .orElseThrow(() -> new CustomException(ErrorCode.RECORD_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Optional<RecordEntity> getClimbingMemberRecordOpt(ClimbingMemberEntity climbingMember,
        BookEntity book) {
        return recordRepository.findByMemberAndBook(climbingMember.getMember(), book);
    }

    public RecordEntity saveRecord(RecordEntity record) {
        return recordRepository.save(record);
    }

    @Transactional(readOnly = true)
    public RecordEntity getRecordById(Long recordId) {
        return recordRepository.findById(recordId)
            .orElseThrow(() -> new CustomException(ErrorCode.RECORD_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public RecordEntity getRecordByMemberAndBook(MemberEntity member, BookEntity book) {
        return recordRepository.findByMemberAndBook(member, book)
            .orElseThrow(() -> new CustomException(ErrorCode.RECORD_NOT_FOUND));
    }

    public void deleteRecord(Long recordId) {
        if (!recordRepository.existsById(recordId)) {
            throw new CustomException(ErrorCode.RECORD_NOT_FOUND);
        }
        recordRepository.deleteById(recordId);
    }

    @Transactional(readOnly = true)
    public List<RecordEntity> getRecordsByStatus(ReadingStatus status) {
        return recordRepository.findAllByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<RecordEntity> getRecordsByMember(MemberEntity member) {
        return recordRepository.findAllByMember(member);
    }

    @Transactional(readOnly = true)
    public boolean existsByMemberAndBook(MemberEntity currentMember, BookEntity book) {
        return recordRepository.existsByMemberAndBook(currentMember, book);
    }

    @Transactional(readOnly = true)
    public Optional<RecordEntity> getRecordOptByMemberAndBook(MemberEntity currentMember,
        BookEntity book) {
        return recordRepository.findByMemberAndBook(currentMember, book);
    }
}
