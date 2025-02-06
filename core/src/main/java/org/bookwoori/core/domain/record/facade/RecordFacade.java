package org.bookwoori.core.domain.record.facade;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
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
import org.bookwoori.core.domain.record.dto.response.RecordListResponseDto;
import org.bookwoori.core.domain.record.entity.ReadingStatus;
import org.bookwoori.core.domain.record.entity.Record;
import org.bookwoori.core.domain.record.service.RecordService;
import org.bookwoori.core.domain.review.dto.response.ReviewUnitDto;
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
        @GrantExp(type = ExpType.READ_PAGE)
    })
    public void createRecord(RecordRequestDto requestDto) {
        Member currentMember = memberService.getCurrentMember();
        Book book = bookService.getOrCreateBookByIsbn(requestDto.isbn13());
        if (recordService.existsByMemberAndBook(currentMember, book)) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_RECORD);
        }
        Record record = requestDto.toRecordEntity(currentMember, book);
        recordService.saveRecord(record);
    }

    @GrantExpContainer({
        @GrantExp(type = ExpType.READ_PAGE)
    })
    public void updateRecord(Long recordId, RecordRequestDto requestDto) {
        Member currentMember = memberService.getCurrentMember();
        Record record = recordService.getRecordById(recordId);
        record.updateRecord(requestDto.toRecordEntity(currentMember, record.getBook()));
    }

    public void deleteRecordAndReview(Long recordId) {
        recordService.deleteRecord(recordId);
    }


    @Transactional(readOnly = true)
    public List<RecordListResponseDto> getRecordsByStatus(ReadingStatus status) {
        List<Record> recordList = recordService.getRecordsByStatus(status);
        if (recordList.isEmpty()) {
            return Collections.emptyList();
        }
        return recordList.stream()
            .map(RecordListResponseDto::from)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RecordDetailsResponseDto getReviewsDetails(Long recordId) {
        Record record = recordService.getRecordById(recordId);
        List<Review> reviewList = reviewService.getReviewListByRecordId(recordId);
        List<ReviewUnitDto> reviewDtoList = reviewList.stream()
            .map(ReviewUnitDto::from)
            .collect(Collectors.toList());
        return RecordDetailsResponseDto.from(record, reviewDtoList);
    }
}
