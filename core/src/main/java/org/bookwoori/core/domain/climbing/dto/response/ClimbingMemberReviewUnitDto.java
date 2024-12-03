package org.bookwoori.core.domain.climbing.dto.response;

import java.util.List;
import lombok.Builder;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingMember;
import org.bookwoori.core.domain.review.entity.Review;

@Builder
public record ClimbingMemberReviewUnitDto(
    Long memberId,
    String nickname,
    String profileImg,
    int star,
    Long reviewId,
    String content,
    List<ReviewEmojiListCountDto> reviewEmojiList) {

    public static ClimbingMemberReviewUnitDto from(ClimbingMember member, Review review,
        List<ReviewEmojiListCountDto> reviewEmojiList) {
        return ClimbingMemberReviewUnitDto.builder()
            .memberId(member.getMember().getMemberId())
            .nickname(member.getMember().getNickname())
            .profileImg(member.getMember().getProfileImg())
            .star(review.getRecord().getStar())
            .reviewId(review.getReviewId())
            .content(review.getContent())
            .reviewEmojiList(reviewEmojiList)
            .build();
    }
}
