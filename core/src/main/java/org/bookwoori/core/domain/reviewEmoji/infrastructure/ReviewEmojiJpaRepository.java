package org.bookwoori.core.domain.reviewEmoji.infrastructure;

import java.util.List;
import java.util.Optional;
import org.bookwoori.core.domain.climbing.infrastructure.ClimbingEntity;
import org.bookwoori.core.domain.member.infrastructure.MemberEntity;
import org.bookwoori.core.domain.review.infrastructure.ReviewEntity;
import org.bookwoori.core.domain.reviewEmoji.entity.EmojiType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewEmojiJpaRepository extends JpaRepository<ReviewEmojiEntity, Long> {

    List<ReviewEmojiEntity> findByClimbingAndReviewIn(ClimbingEntity climbing,
        List<ReviewEntity> sharedReviews);

    Optional<ReviewEmojiEntity> findByMemberAndClimbingAndReviewAndEmoji(MemberEntity member,
        ClimbingEntity climbing,
        ReviewEntity review, EmojiType emoji);

    List<ReviewEmojiEntity> findByReview(ReviewEntity review);
}
