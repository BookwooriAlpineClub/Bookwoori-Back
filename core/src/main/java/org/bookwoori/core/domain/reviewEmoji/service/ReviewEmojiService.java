package org.bookwoori.core.domain.reviewEmoji.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.review.entity.Review;
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
    public List<ReviewEmoji> findByReview(Review review) {
        return reviewEmojiRepository.findByReview(review);
    }
}
