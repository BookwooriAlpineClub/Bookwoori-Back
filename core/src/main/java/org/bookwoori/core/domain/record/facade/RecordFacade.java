package org.bookwoori.core.domain.record.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bookwoori.core.domain.book.infrastructure.BookEntity;
import org.bookwoori.core.domain.book.service.BookServiceImpl;
import org.bookwoori.core.domain.member.infrastructure.MemberEntity;
import org.bookwoori.core.domain.member.service.MemberServiceImpl;
import org.bookwoori.core.domain.record.dto.request.RecordRequestDto;
import org.bookwoori.core.domain.record.dto.response.RecordDetailsResponseDto;
import org.bookwoori.core.domain.record.dto.response.RecordResponseDto;
import org.bookwoori.core.domain.record.dto.response.ReviewResponseDto;
import org.bookwoori.core.domain.record.entity.ReadingStatus;
import org.bookwoori.core.domain.record.infrastructure.RecordEntity;
import org.bookwoori.core.domain.record.service.RecordServiceImpl;
import org.bookwoori.core.domain.review.infrastructure.ReviewEntity;
import org.bookwoori.core.domain.review.service.ReviewServiceImpl;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Log4j2
@Transactional
public class RecordFacade {

    private final RecordServiceImpl recordService;
    private final MemberServiceImpl memberService;
    private final BookServiceImpl bookService;
    private final ReviewServiceImpl reviewService;

    public void createRecord(RecordRequestDto requestDto) {
        MemberEntity currentMember = memberService.getCurrentMember();
        BookEntity book = bookService.getOrCreateBookByIsbn(requestDto.isbn13());
        if (recordService.existsByMemberAndBook(currentMember, book)) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_RECORD);
        }
        RecordEntity record = requestDto.toRecordEntity(currentMember, book);
        RecordEntity newRecord = recordService.saveRecord(record);
        if (requestDto.reviewContent() != null && !requestDto.reviewContent().isBlank()) {
            if (reviewService.existsReviewByMemberAndBook(currentMember, book)) {
                throw new CustomException(ErrorCode.ALREADY_EXIST_REVIEW);
            }
            reviewService.saveReview(
                requestDto.toReviewEntity(newRecord, requestDto.reviewContent()));
        }
    }

    public void createReview(RecordRequestDto requestDto) {
        MemberEntity currentMember = memberService.getCurrentMember();
        BookEntity book = bookService.getOrCreateBookByIsbn(requestDto.isbn13());
        RecordEntity record = recordService.getRecordByMemberAndBook(currentMember, book);
        if (reviewService.existsReviewByMemberAndBook(currentMember, book)) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_REVIEW);
        }
        reviewService.saveReview(requestDto.toReviewEntity(record, requestDto.reviewContent()));
    }

    public void updateRecord(Long recordId, RecordRequestDto requestDto) {
        MemberEntity currentMember = memberService.getCurrentMember();
        RecordEntity record = recordService.getRecordById(recordId);
        // Record 업데이트
        BookEntity book = bookService.getOrCreateBookByIsbn(requestDto.isbn13());
        record.updateRecord(requestDto.toRecordEntity(currentMember, record.getBook()));
        // Review 업데이트 (reviewContent가 있을 경우만)
        if (requestDto.reviewContent() != null && !requestDto.reviewContent().isBlank()) {
            ReviewEntity review = reviewService.getReviewByRecordId(recordId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND)); // 리뷰가 없으면 예외
            review.updateReview(requestDto.reviewContent());
        }
    }


    public void updateReview(Long recordId, RecordRequestDto requestDto) {
        ReviewEntity review = reviewService.getReviewByRecordId(recordId)
            .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
        review.updateReview(requestDto.reviewContent());

    }

    public void deleteRecordAndReview(Long recordId) {
        if (reviewService.getReviewByRecordId(recordId).isPresent()) {
            reviewService.deleteReviewByRecordId(recordId);
        }
        recordService.deleteRecord(recordId);
    }


    @Transactional(readOnly = true)
    public List<RecordResponseDto> getRecordsByStatus(ReadingStatus status) {
        List<RecordResponseDto> recordResponseDtoList = new ArrayList<>();
        List<RecordEntity> recordList = recordService.getRecordsByStatus(status);
        recordList.stream().forEach(record -> {
            String reviewContent = reviewService.getReviewByRecordId(record.getRecordId())
                .map(ReviewEntity::getContent)
                .orElse(null);
            RecordResponseDto recordResponseDto = RecordResponseDto.from(record, reviewContent);
            recordResponseDtoList.add(recordResponseDto);
        });
        return recordResponseDtoList;
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviews() {
        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();
        List<RecordEntity> recordList = recordService.getRecordsByMember(
            memberService.getCurrentMember());
        recordList.stream().forEach(record -> {
            if (reviewService.getReviewByRecordId(record.getRecordId()).isPresent()) {
                ReviewEntity review = reviewService.getReviewByRecordId(record.getRecordId())
                    .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
                ReviewResponseDto reviewResponseDto = ReviewResponseDto.from(record, review);
                reviewResponseDtoList.add(reviewResponseDto);
            }
        });
        return reviewResponseDtoList;
    }

    @Transactional(readOnly = true)
    public RecordDetailsResponseDto getReviewsDetails(Long recordId) {
        RecordEntity record = recordService.getRecordById(recordId);
        Optional<ReviewEntity> review = reviewService.getReviewByRecordId(recordId);
        return RecordDetailsResponseDto.from(record, review);
    }
}
