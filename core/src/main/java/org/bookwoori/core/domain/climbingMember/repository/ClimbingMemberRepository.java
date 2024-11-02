package org.bookwoori.core.domain.climbingMember.repository;

import java.util.List;
import java.util.Optional;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingMember;
import org.bookwoori.core.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClimbingMemberRepository extends JpaRepository<ClimbingMember, Long> {

    Optional<ClimbingMember> findByMemberAndClimbing(Member member, Climbing climbing);

    boolean existsByMemberAndClimbing(Member member, Climbing climbing);

    int countByClimbing(Climbing climbing);

    List<ClimbingMember> findByClimbing(Climbing climbing);

    Optional<ClimbingMember> findByMemberAndClimbing_Id(Member member, Long climbingId);
}
