package org.bookwoori.core.domain.record.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bookwoori.core.domain.book.entity.Book;
import org.bookwoori.core.domain.book.service.BookService;
import org.bookwoori.core.domain.exp.annotation.GrantExp;
import org.bookwoori.core.domain.exp.annotation.GrantExpContainer;
import org.bookwoori.core.domain.exp.entity.ExpType;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.member.service.MemberService;
import org.bookwoori.core.domain.record.dto.request.RecordRequestDto;
import org.bookwoori.core.domain.record.dto.response.RecordDetailsResponseDto;
import org.bookwoori.core.domain.record.dto.response.RecordResponseDto;
import org.bookwoori.core.domain.record.dto.response.ReviewResponseDto;
import org.bookwoori.core.domain.record.entity.ReadingStatus;
import org.bookwoori.core.domain.record.entity.Record;
import org.bookwoori.core.domain.record.service.RecordService;
import org.bookwoori.core.domain.review.entity.Review;
import org.bookwoori.core.domain.review.service.ReviewService;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Log4j2
@Transactional
public class RecordFacade {

    private final RecordService recordService;
    private final MemberService memberService;
    private final BookService bookService;
    private final ReviewService reviewService;

    @GrantExpContainer({
        @GrantExp(type = ExpType.READ_PAGE),
        @GrantExp(type = ExpType.ADD_STAR),
        @GrantExp(type = ExpType.WRITE_REVIEW)
    })
    public void createRecord(RecordRequestDto requestDto) {
        Member currentMember = memberService.getCurrentMember();
        Book book = bookService.getOrCreateBookByIsbn(requestDto.isbn13());
        if (recordService.existsByMemberAndBook(currentMember, book)) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_RECORD);
        }
        Record record = requestDto.toRecordEntity(currentMember, book);
        Record newRecord = recordService.saveRecord(record);
        if (requestDto.reviewContent() != null && !requestDto.reviewContent().isBlank()) {
            if (reviewService.existsReviewByMemberAndBook(currentMember, book)) {
                throw new CustomException(ErrorCode.ALREADY_EXIST_REVIEW);
            }
            reviewService.saveReview(
                requestDto.toReviewEntity(newRecord, requestDto.reviewContent()));
        }
    }

    @GrantExpContainer({
        @GrantExp(type = ExpType.READ_PAGE),
        @GrantExp(type = ExpType.ADD_STAR),
        @GrantExp(type = ExpType.WRITE_REVIEW)
    })
    public void updateRecord(Long recordId, RecordRequestDto requestDto) {
        Member currentMember = memberService.getCurrentMember();
        Record record = recordService.getRecordById(recordId);
        // Record 업데이트
        record.updateRecord(requestDto.toRecordEntity(currentMember, record.getBook()));
        // Review 업데이트
        if (reviewService.getReviewByRecordId(
            record.getRecordId()).isPresent()) {
            // Review 업데이트
            Review review = reviewService.getReviewByRecordId(recordId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND)); // 리뷰가 없으면 예외
            review.updateReview(requestDto.reviewContent());
        }
        else if (requestDto.reviewContent() != null && reviewService.getReviewByRecordId(
            record.getRecordId()).isEmpty()){
            // Review 생성
            reviewService.saveReview(requestDto.toReviewEntity(record, requestDto.reviewContent()));
        }
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
        List<Record> recordList = recordService.getRecordsByStatus(status);
        recordList.stream().forEach(record -> {
            String reviewContent = reviewService.getReviewByRecordId(record.getRecordId())
                .map(Review::getContent)
                .orElse(null);
            RecordResponseDto recordResponseDto = RecordResponseDto.from(record, reviewContent);
            recordResponseDtoList.add(recordResponseDto);
        });
        return recordResponseDtoList;
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviews() {
        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();
        List<Record> recordList = recordService.getRecordsByMember(
            memberService.getCurrentMember());
        recordList.stream().forEach(record -> {
            if (reviewService.getReviewByRecordId(record.getRecordId()).isPresent()) {
                Review review = reviewService.getReviewByRecordId(record.getRecordId())
                    .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
                ReviewResponseDto reviewResponseDto = ReviewResponseDto.from(record, review);
                reviewResponseDtoList.add(reviewResponseDto);
            }
        });
        return reviewResponseDtoList;
    }

    @Transactional(readOnly = true)
    public RecordDetailsResponseDto getReviewsDetails(Long recordId) {
        Record record = recordService.getRecordById(recordId);
        Optional<Review> review = reviewService.getReviewByRecordId(recordId);
        return RecordDetailsResponseDto.from(record, review);
    }
}
