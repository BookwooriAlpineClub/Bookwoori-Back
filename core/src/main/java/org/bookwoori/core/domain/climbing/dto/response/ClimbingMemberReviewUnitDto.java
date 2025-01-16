package org.bookwoori.core.domain.climbing.dto.response;

import java.util.List;
import lombok.Builder;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingMember;
import org.bookwoori.core.domain.review.entity.Review;

@Builder
public record ClimbingMemberReviewUnitDto(
    Long memberId,
    String nickname,
    int star,
    Long reviewId,
    String content,
    List<ReviewEmojiListCountDto> reviewEmojiList) {

    public static ClimbingMemberReviewUnitDto from(ClimbingMember member, Review review,
        List<ReviewEmojiListCountDto> reviewEmojiList) {
        return ClimbingMemberReviewUnitDto.builder()
            .memberId(member.getMember().getMemberId())
            .nickname(member.getMember().getNickname())
            .star(review.getRecord().getStarReview())
            .reviewId(review.getReviewId())
            .content(review.getContentReview())
            .reviewEmojiList(reviewEmojiList)
            .build();
    }
}
