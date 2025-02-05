package org.bookwoori.core.domain.climbingReview.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingMember;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingRole;
import org.bookwoori.core.domain.climbingReview.entity.ClimbingReview;
import org.bookwoori.core.domain.climbingReview.repository.ClimbingReviewRepository;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.review.entity.Review;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClimbingReviewService {

  private final ClimbingReviewRepository climbingReviewRepository;

  public void save(ClimbingReview climbingReview) {
    climbingReviewRepository.save(climbingReview);
  }

  public ClimbingReview findByClimbingMemberId(Long climbingMemberId) {
    return climbingReviewRepository.findByClimbingMember_ClimbingMemberId(climbingMemberId);
  }

}
