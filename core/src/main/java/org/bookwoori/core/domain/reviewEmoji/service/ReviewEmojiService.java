package org.bookwoori.core.domain.reviewEmoji.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.review.entity.Review;
import org.bookwoori.core.domain.reviewEmoji.entity.Emoji;
import org.bookwoori.core.domain.reviewEmoji.entity.ReviewEmoji;
import org.bookwoori.core.domain.reviewEmoji.repository.ReviewEmojiRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewEmojiService {

    private final ReviewEmojiRepository reviewEmojiRepository;

    public ReviewEmoji save(ReviewEmoji reviewEmoji) {
        return reviewEmojiRepository.save(reviewEmoji);
    }

    @Transactional(readOnly = true)
    public List<ReviewEmoji> findByReviewAndEmoji(Review review, Emoji emoji) {
        return reviewEmojiRepository.findByReviewAndEmoji(review, emoji);
    }

    @Transactional(readOnly = true)
    public List<ReviewEmoji> findByClimbingAndReviews(Climbing climbing,
        List<Review> sharedReviews) {
        return reviewEmojiRepository.findByClimbingAndReviewIn(climbing, sharedReviews);
    }
}
