package org.bookwoori.core.domain.climbingMember.repository;

import aj.org.objectweb.asm.commons.Remapper;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingMember;
import org.bookwoori.core.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClimbingMemberRepository extends JpaRepository<ClimbingMember, Long> {
    Optional<ClimbingMember> findByMemberAndClimbing(Member member, Climbing climbing);
}
