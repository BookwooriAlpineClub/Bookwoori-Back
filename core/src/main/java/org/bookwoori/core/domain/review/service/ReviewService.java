package org.bookwoori.core.domain.review.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.book.entity.Book;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.review.entity.Review;
import org.bookwoori.core.domain.review.repository.ReviewRepository;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

  private final ReviewRepository reviewRepository;

  @Transactional(readOnly = true)
  public Review getReviewById(Long reviewId) {
    return reviewRepository.findById(reviewId)
        .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
  }

  @Transactional(readOnly = true)
  public List<Review> getReviewsByMembersAndBook(List<Long> members, Book book) {
    return reviewRepository.findByMemberIdsAndBook(members, book);
  }

  @Transactional(readOnly = true)
  public Review getReviewByMemberAndBook(Member member, Book book) {
    return reviewRepository.findByMemberAndBook(member, book);
  }

  public boolean existsReviewByMemberAndBook(Member member, Book book) {
    return reviewRepository.existsByRecord_MemberAndRecord_Book(member, book);
  }


  @Transactional
  public void saveReview(Review review) {
    reviewRepository.save(review);
  }

  @Transactional(readOnly = true)
  public List<Review> getReviewListByRecordId(Long recordId) {
    return reviewRepository.findAllByRecord_RecordId(recordId);
  }

  public void deleteReviewByRecordId(Long recordId) {
    reviewRepository.deleteByRecord_RecordId(recordId);
  }

  public void deleteReview(Long reviewId) {
    reviewRepository.deleteById(reviewId);
  }
}