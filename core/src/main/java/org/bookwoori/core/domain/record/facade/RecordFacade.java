package org.bookwoori.core.domain.record.facade;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bookwoori.core.domain.book.entity.Book;
import org.bookwoori.core.domain.book.service.BookService;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.member.service.MemberService;
import org.bookwoori.core.domain.record.dto.request.RecordRequestDto;
import org.bookwoori.core.domain.record.dto.response.RecordResponseDto;
import org.bookwoori.core.domain.record.dto.response.ReviewResponseDto;
import org.bookwoori.core.domain.record.entity.ReadingStatus;
import org.bookwoori.core.domain.record.entity.Record;
import org.bookwoori.core.domain.record.service.RecordService;
import org.bookwoori.core.domain.review.entity.Review;
import org.bookwoori.core.domain.review.service.ReviewService;
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

    public void createRecordAndReview(RecordRequestDto requestDto) {
        Member currentMember = memberService.getCurrentMember();
        Book book = bookService.getOrCreateBookByIsbn(requestDto.isbn13());

        Record savedRecord = recordService.saveRecord(
            requestDto.toRecordEntity(currentMember, book));
        reviewService.saveReview(
            requestDto.toReviewEntity(savedRecord, requestDto.reviewContent()));

    }

    public void updateRecordAndReview(Long recordId, RecordRequestDto requestDto) {
        Member currentMember = memberService.getCurrentMember();
        Record record = recordService.getRecordById(recordId);
        Review review = reviewService.getReviewByRecordId(recordId);

        record.updateRecord(requestDto.toRecordEntity(currentMember, record.getBook()));
        review.updateReview(requestDto.reviewContent());

    }

    public void deleteRecordAndReview(Long recordId) {
        reviewService.deleteReviewByRecordId(recordId);
        recordService.deleteRecord(recordId);
    }

    @Transactional(readOnly = true)
    public List<RecordResponseDto> getRecordsByStatus(ReadingStatus status) {

        List<RecordResponseDto> recordResponseDtoList = new ArrayList<>();
        List<Record> recordList = recordService.getRecordsByStatus(status);

        recordList.stream().forEach(record -> {
            RecordResponseDto recordResponseDto = RecordResponseDto.from(record);
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

            Review review = reviewService.getReviewByRecordId(record.getRecordId());
            ReviewResponseDto reviewResponseDto = ReviewResponseDto.from(record, review);
            reviewResponseDtoList.add(reviewResponseDto);
        });

        return reviewResponseDtoList;

    }


}
