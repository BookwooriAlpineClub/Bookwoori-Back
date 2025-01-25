package org.bookwoori.core.domain.reviewEmoji.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.climbing.infrastructure.ClimbingEntity;
import org.bookwoori.core.domain.member.infrastructure.MemberEntity;
import org.bookwoori.core.domain.review.infrastructure.ReviewEntity;
import org.bookwoori.core.domain.reviewEmoji.entity.EmojiType;
import org.bookwoori.core.domain.reviewEmoji.infrastructure.ReviewEmojiEntity;
import org.bookwoori.core.domain.reviewEmoji.infrastructure.ReviewEmojiJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewEmojiServiceImpl {

    private final ReviewEmojiJpaRepository reviewEmojiRepository;

    public ReviewEmojiEntity save(ReviewEmojiEntity reviewEmoji) {
        return reviewEmojiRepository.save(reviewEmoji);
    }

    public void deleteEmoji(MemberEntity member, ClimbingEntity climbing, ReviewEntity review,
        EmojiType emoji) {
        Optional<ReviewEmojiEntity> reviewEmoji = reviewEmojiRepository.findByMemberAndClimbingAndReviewAndEmoji(
            member, climbing, review, emoji);
        reviewEmojiRepository.delete(reviewEmoji.get());
    }

    @Transactional(readOnly = true)
    public List<ReviewEmojiEntity> getEmojisByClimbingAndReviews(ClimbingEntity climbing,
        List<ReviewEntity> sharedReviews) {
        return reviewEmojiRepository.findByClimbingAndReviewIn(climbing, sharedReviews);
    }

    @Transactional(readOnly = true)
    public Optional<ReviewEmojiEntity> getEmojisOpt(MemberEntity currentMember,
        ClimbingEntity climbing, ReviewEntity review, EmojiType emoji) {
        return reviewEmojiRepository.findByMemberAndClimbingAndReviewAndEmoji(currentMember,
            climbing, review, emoji);
    }

    @Transactional(readOnly = true)
    public List<ReviewEmojiEntity> getEmojisByReview(ReviewEntity review) {
        return reviewEmojiRepository.findByReview(review);
    }
}
