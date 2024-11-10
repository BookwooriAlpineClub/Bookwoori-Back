package org.bookwoori.core.domain.review.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.book.entity.Book;
import org.bookwoori.core.domain.review.entity.Review;
import org.bookwoori.core.domain.review.repository.ReviewRepository;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
            .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<Review> findByMembersAndBook(List<Long> members, Book book) {
        return reviewRepository.findByMemberIdsAndBook(members, book);
    }
}
