package org.bookwoori.core.domain.climbingReview.repository;

import org.bookwoori.core.domain.climbingReview.entity.ClimbingReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClimbingReviewRepository extends JpaRepository<ClimbingReview, Long> {

  ClimbingReview findByClimbingMember_ClimbingMemberId(Long climbingMemberId);
}
