package org.bookwoori.core.domain.reviewEmoji.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.review.entity.Review;
import org.bookwoori.core.domain.reviewEmoji.entity.EmojiType;
import org.bookwoori.core.domain.reviewEmoji.entity.ReviewEmoji;
import org.bookwoori.core.domain.reviewEmoji.repository.ReviewEmojiRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewEmojiServiceImpl implements ReviewEmojiService {

    private final ReviewEmojiRepository reviewEmojiRepository;

    public ReviewEmoji save(ReviewEmoji reviewEmoji) {
        return reviewEmojiRepository.save(reviewEmoji);
    }

    public void deleteEmoji(Member member, Climbing climbing, Review review, EmojiType emoji) {
        Optional<ReviewEmoji> reviewEmoji = reviewEmojiRepository.findByMemberAndClimbingAndReviewAndEmoji(
            member, climbing, review, emoji);
        reviewEmojiRepository.delete(reviewEmoji.get());
    }

    @Transactional(readOnly = true)
    public List<ReviewEmoji> getEmojisByClimbingAndReviews(Climbing climbing,
        List<Review> sharedReviews) {
        return reviewEmojiRepository.findByClimbingAndReviewIn(climbing, sharedReviews);
    }

    @Transactional(readOnly = true)
    public Optional<ReviewEmoji> getEmojisOpt(Member currentMember,
        Climbing climbing, Review review, EmojiType emoji) {
        return reviewEmojiRepository.findByMemberAndClimbingAndReviewAndEmoji(currentMember,
            climbing, review, emoji);
    }

    @Transactional(readOnly = true)
    public List<ReviewEmoji> getEmojisByReview(Review review) {
        return reviewEmojiRepository.findByReview(review);
    }
}
