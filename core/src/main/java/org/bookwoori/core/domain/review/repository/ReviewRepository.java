package org.bookwoori.core.domain.review.repository;

import org.bookwoori.core.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
