package org.bookwoori.core.domain.review.service;

import java.util.List;
import java.util.Optional;
import org.bookwoori.core.domain.book.entity.Book;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.review.entity.Review;

public interface ReviewService {

  Review getReviewById(Long reviewId);

  List<Review> getReviewsByMembersAndBook(List<Long> members, Book book);

  Review getReviewByMemberAndBook(Member member, Book book);

  boolean existsReviewByMemberAndBook(Member member, Book book);

  void saveReview(Review review);

  Optional<Review> getReviewByRecordId(Long recordId);

  void deleteReviewByRecordId(Long recordId);

}
