package org.bookwoori.core.domain.climbing.dto.response;

import java.util.List;
import lombok.Builder;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingMember;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.record.entity.ReadingStatus;
import org.bookwoori.core.domain.review.entity.Review;

@Builder
public record ClimbingMemberReviewUnitDto(
    ReadingStatus readingStatus,
    Long memberId,
    String nickname,
    String profileImg,
    int star,
    Long reviewId,
    String content,
    List<ReviewEmojiListCountDto> reviewEmojiList) {

    public static ClimbingMemberReviewUnitDto from(Member member, Review review,
        List<ReviewEmojiListCountDto> reviewEmojiList) {
        return ClimbingMemberReviewUnitDto.builder()
            .readingStatus(review.getRecord().getStatus())
            .memberId(member.getMemberId())
            .nickname(member.getNickname())
            .profileImg(member.getProfileImg())
            .star(review.getStar())
            .reviewId(review.getReviewId())
            .content(review.getContent())
            .reviewEmojiList(reviewEmojiList)
            .build();
    }
}
