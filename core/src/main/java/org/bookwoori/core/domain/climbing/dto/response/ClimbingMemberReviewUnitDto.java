package org.bookwoori.core.domain.climbing.dto.response;

import lombok.Builder;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingMember;
import org.bookwoori.core.domain.review.entity.Review;

@Builder
public record ClimbingMemberReviewUnitDto(
    Long memberId,
    String nickname,
    int star,
    Long reviewId,
    String content
//    List<ReviewEmoji> emojiList
) {

    public static ClimbingMemberReviewUnitDto from(ClimbingMember member, Review review) {
        return ClimbingMemberReviewUnitDto.builder()
            .memberId(member.getMember().getMemberId())
            .nickname(member.getMember().getNickname())
            .star(review.getRecord().getStar())
            .reviewId(review.getReviewId())
            .content(review.getContent())
//            .emojiList(emojiList)
            .build();
    }
}
