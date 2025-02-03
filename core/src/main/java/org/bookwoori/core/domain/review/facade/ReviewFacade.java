package org.bookwoori.core.domain.review.facade;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.exp.annotation.GrantExp;
import org.bookwoori.core.domain.exp.annotation.GrantExpContainer;
import org.bookwoori.core.domain.exp.entity.ExpType;
import org.bookwoori.core.domain.member.service.MemberService;
import org.bookwoori.core.domain.record.dto.response.ReviewListResponseDto;
import org.bookwoori.core.domain.record.entity.Record;
import org.bookwoori.core.domain.record.service.RecordService;
import org.bookwoori.core.domain.review.dto.request.ReviewRequestDto;
import org.bookwoori.core.domain.review.dto.response.ReviewUnitDto;
import org.bookwoori.core.domain.review.entity.Review;
import org.bookwoori.core.domain.review.service.ReviewService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class ReviewFacade {

  private final ReviewService reviewService;
  private final RecordService recordService;
  private final MemberService memberService;

  @GrantExpContainer({
      @GrantExp(type = ExpType.ADD_STAR),
      @GrantExp(type = ExpType.WRITE_REVIEW)
  })
  public void createReview(Long recordId, ReviewRequestDto requestDto) {
    Record record = recordService.getRecordById(recordId);
    reviewService.saveReview(requestDto.toEntity(record));
    }

  @GrantExpContainer({
      @GrantExp(type = ExpType.ADD_STAR),
      @GrantExp(type = ExpType.WRITE_REVIEW)
  })
  public void updateReview(Long reviewId, ReviewRequestDto requestDto) {
    Review review = reviewService.getReviewById(reviewId);
    review.updateReview(requestDto.star(), requestDto.content());
  }

  public void deleteReview(Long reviewId) {
    reviewService.deleteReview(reviewId);
  }

  @Transactional(readOnly = true)
  public List<ReviewListResponseDto> getReviews() {
    List<Record> recordList = recordService.getRecordsByMember(memberService.getCurrentMember());
    return recordList.stream()
        .map(record -> {
          List<ReviewUnitDto> reviewList = reviewService.getReviewListByRecordId(record.getRecordId()).stream()
              .map(ReviewUnitDto::from)
              .sorted(Comparator
                  .comparing((ReviewUnitDto review) -> record.getEndDate(), Comparator.nullsLast(Comparator.reverseOrder()))
                  .thenComparing(ReviewUnitDto::reviewId, Comparator.reverseOrder())
              )
              .collect(Collectors.toList());
          return ReviewListResponseDto.from(record, reviewList);
        })
        .collect(Collectors.toList());
  }
}