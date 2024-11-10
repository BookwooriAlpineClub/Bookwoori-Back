package org.bookwoori.core.domain.reviewEmoji.repository;

import java.util.List;
import java.util.Optional;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.review.entity.Review;
import org.bookwoori.core.domain.reviewEmoji.entity.Emoji;
import org.bookwoori.core.domain.reviewEmoji.entity.ReviewEmoji;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewEmojiRepository extends JpaRepository<ReviewEmoji, Long> {

    List<ReviewEmoji> findByClimbingAndReviewIn(Climbing climbing, List<Review> sharedReviews);

    List<ReviewEmoji> findByReviewAndEmoji(Review review, Emoji emoji);

    Optional<ReviewEmoji> findByMemberAndClimbingAndReviewAndEmoji(Member member, Climbing climbing,
        Review review, Emoji emoji);

    List<ReviewEmoji> findByReview(Review review);
}
