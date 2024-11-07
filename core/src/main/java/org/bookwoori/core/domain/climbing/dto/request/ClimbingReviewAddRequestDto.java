package org.bookwoori.core.domain.climbing.dto.request;

import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.review.entity.Review;
import org.bookwoori.core.domain.reviewEmoji.entity.Emoji;
import org.bookwoori.core.domain.reviewEmoji.entity.ReviewEmoji;

public record ClimbingReviewAddRequestDto(
    Emoji emoji
) {

    public ReviewEmoji toEntity(Member member, Climbing climbing, Review review) {
        return ReviewEmoji.builder()
            .climbing(climbing)
            .review(review)
            .member(member)
            .emoji(emoji)
            .build();
    }

}
