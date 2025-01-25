package org.bookwoori.core.domain.review.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.book.infrastructure.BookEntity;
import org.bookwoori.core.domain.member.infrastructure.MemberEntity;
import org.bookwoori.core.domain.review.infrastructure.ReviewEntity;
import org.bookwoori.core.domain.review.infrastructure.ReviewJpaRepository;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl {

    private final ReviewJpaRepository reviewRepository;

    @Transactional(readOnly = true)
    public ReviewEntity getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
            .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<ReviewEntity> getReviewsByMembersAndBook(List<Long> members, BookEntity book) {
        return reviewRepository.findByMemberIdsAndBook(members, book);
    }

    @Transactional(readOnly = true)
    public ReviewEntity getReviewByMemberAndBook(MemberEntity member, BookEntity book) {
        return reviewRepository.findByMemberAndBook(member, book);
    }

    public boolean existsReviewByMemberAndBook(MemberEntity member, BookEntity book) {
        return reviewRepository.existsByRecord_MemberAndRecord_Book(member, book);
    }


    @Transactional
    public void saveReview(ReviewEntity review) {
        reviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    public Optional<ReviewEntity> getReviewByRecordId(Long recordId) {
        return reviewRepository.findByRecord_RecordId(recordId);
    }

    public void deleteReviewByRecordId(Long recordId) {
        reviewRepository.deleteByRecord_RecordId(recordId);
    }
}
