package org.bookwoori.core.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.member.entity.Member;
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
    public Review getReviewByMemberAndClimbing(Member currentMember, Climbing climbing) {
        return reviewRepository.findByRecordMemberAndRecordBook(currentMember, climbing.getBook())
            .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
    }
}
