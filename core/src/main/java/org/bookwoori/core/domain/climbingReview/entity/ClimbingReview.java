package org.bookwoori.core.domain.climbingReview.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingMember;
import org.bookwoori.core.domain.review.entity.Review;

@Entity
@Table(name = "climbing_review")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ClimbingReview {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "climbing_review_id", updatable = false)
  private Long climbingReviewId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "climbing_member_id", updatable = false)
  @NotNull
  private ClimbingMember climbingMember;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "review_id", updatable = false)
  @NotNull
  private Review review;

  public ClimbingReview(ClimbingMember climbingMember, Review review){
    this.climbingMember = climbingMember;
    this.review = review;
  }
}

