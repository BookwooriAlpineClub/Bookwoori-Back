package org.bookwoori.core.domain.climbingMember.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.climbingMember.repository.ClimbingMemberRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ClimbingMemberRepositoryImpl implements ClimbingMemberRepository {

    private final ClimbingMemberJpaRepository climbingMemberJpaRepository;
}
