package org.bookwoori.core.domain.climbing.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.climbing.repository.ClimbingRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ClimbingRepositoryImpl implements ClimbingRepository {

    private final ClimbingJpaRepository climbingJpaRepository;
}
