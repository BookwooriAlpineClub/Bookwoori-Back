package org.bookwoori.core.domain.reviewEmoji.repository;

import java.util.List;
import java.util.Optional;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.review.entity.Review;
import org.bookwoori.core.domain.reviewEmoji.entity.EmojiType;
import org.bookwoori.core.domain.reviewEmoji.entity.ReviewEmoji;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewEmojiRepository extends JpaRepository<ReviewEmoji, Long> {

    List<ReviewEmoji> findByClimbingAndReviewIn(Climbing climbing, List<Review> sharedReviews);

    Optional<ReviewEmoji> findByMemberAndClimbingAndReviewAndEmoji(Member member, Climbing climbing,
        Review review, EmojiType emoji);

    List<ReviewEmoji> findByReview(Review review);

    boolean existsByReviewAndMemberAndEmoji(Review review, Member member, EmojiType emojiType);
}
