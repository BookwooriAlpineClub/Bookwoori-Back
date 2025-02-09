package org.bookwoori.core.domain.climbing.facade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bookwoori.core.domain.climbing.dto.response.ClimbingMemberReviewUnitDto;
import org.bookwoori.core.domain.climbing.dto.response.ClimbingReviewListResponseDto;
import org.bookwoori.core.domain.climbing.dto.response.ReviewEmojiListCountDto;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.climbing.service.ClimbingService;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingMember;
import org.bookwoori.core.domain.climbingMember.service.ClimbingMemberService;
import org.bookwoori.core.domain.climbingReview.entity.ClimbingReview;
import org.bookwoori.core.domain.climbingReview.service.ClimbingReviewService;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.member.service.MemberService;
import org.bookwoori.core.domain.review.entity.Review;
import org.bookwoori.core.domain.review.service.ReviewService;
import org.bookwoori.core.domain.reviewEmoji.entity.EmojiType;
import org.bookwoori.core.domain.reviewEmoji.entity.ReviewEmoji;
import org.bookwoori.core.domain.reviewEmoji.service.ReviewEmojiService;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ClimbingReviewFacade {

  private final MemberService memberService;
  private final ClimbingService climbingService;
  private final ClimbingMemberService climbingMemberService;
  private final ReviewService reviewService;
  private final ReviewEmojiService reviewEmojiService;
  private final ClimbingReviewService climbingReviewService;

  public void shareReviewToClimbing(Long climbingId, Long reviewId) {
    Member currentMember = memberService.getCurrentMember();
    ClimbingMember climbingMember = climbingMemberService.getMemberInClimbing(currentMember,
        climbingId);
    if (climbingMember.isHasShared()) {
      throw new CustomException(ErrorCode.REVIEW_ALREADY_SHARED);
    }
    Review review = reviewService.getReviewById(reviewId);
    ClimbingReview climbingReview = ClimbingReview.builder()
        .climbingMember(climbingMember)
        .review(review)
        .build();

    climbingReviewService.save(climbingReview);
    climbingMember.updateShared(true);
  }

  @Transactional(readOnly = true)
  public ClimbingReviewListResponseDto getClimbingReviewList(Long climbingId) {
    Climbing climbing = climbingService.getClimbingById(climbingId);
    List<Long> sharedClimbingMemberIds = climbingMemberService.getSharedClimbingMemberIds(climbing);

    // ClimbingReview 리스트 가져오기
    List<ClimbingReview> climbingReviews = sharedClimbingMemberIds.stream()
        .map(climbingReviewService::findByClimbingMemberId)
        .filter(Objects::nonNull)
        .toList();

    if (climbingReviews.isEmpty()) {
      return new ClimbingReviewListResponseDto(true, Collections.emptyList());
    }

    // Review 리스트 추출
    List<Review> reviews = climbingReviews.stream()
        .map(ClimbingReview::getReview)
        .collect(Collectors.toList());

    // 리뷰가 있을 때만 이모지 데이터 조회
    List<ReviewEmoji> sharedReviewEmojis = reviews.isEmpty()
        ? Collections.emptyList()
        : reviewEmojiService.getEmojisByClimbingAndReviews(climbing, reviews);

    // 리뷰 ID 기준으로 이모지 그룹화
    Map<Long, Map<EmojiType, Long>> reviewEmojiCounts = sharedReviewEmojis.stream()
        .collect(Collectors.groupingBy(
            reviewEmoji -> reviewEmoji.getReview().getReviewId(),
            Collectors.groupingBy(
                ReviewEmoji::getEmoji,
                Collectors.counting()
            )
        ));

    // 공유된 멤버 ID를 순회하며 ClimbingMemberReviewUnitDto 생성
    List<ClimbingMemberReviewUnitDto> climbingMemberReviewUnits = sharedClimbingMemberIds.stream()
        .map(memberId -> {
          Optional<ClimbingReview> climbingReviewOpt = climbingReviews.stream()
              .filter(review -> review.getClimbingMember().getClimbingMemberId().equals(memberId))
              .findFirst();

          return climbingReviewOpt.map(climbingReview -> {
            Review review = climbingReview.getReview();
            Map<EmojiType, Long> emojiCounts = reviewEmojiCounts.getOrDefault(review.getReviewId(), Collections.emptyMap());
            List<ReviewEmojiListCountDto> reviewEmojiList = emojiCounts.entrySet().stream()
                .map(entry -> new ReviewEmojiListCountDto(
                    reviewEmojiService.hasClickedEmoji(review, memberService.getCurrentMember(), entry.getKey()),
                    entry.getKey(),
                    entry.getValue().intValue()
                ))
                .collect(Collectors.toList());

            Member member = climbingMemberService.getClimbingMemberById(memberId).getMember();
            return ClimbingMemberReviewUnitDto.from(member, review, reviewEmojiList);
          }).orElse(null);
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

    return new ClimbingReviewListResponseDto(true, climbingMemberReviewUnits);
  }


}
