package org.bookwoori.core.domain.review.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.review.repository.ReviewRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {

    private final ReviewJpaRepository reviewJpaRepository;
}
