package org.bookwoori.core.domain.reviewEmoji.repository;

import java.util.List;
import org.bookwoori.core.domain.review.entity.Review;
import org.bookwoori.core.domain.reviewEmoji.entity.ReviewEmoji;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewEmojiRepository extends JpaRepository<ReviewEmoji, Long> {

    List<ReviewEmoji> findByReview(Review review);
}
