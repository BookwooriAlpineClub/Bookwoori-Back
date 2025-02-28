package org.bookwoori.core.domain.climbingMember.repository;

import java.util.List;
import java.util.Optional;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingMember;
import org.bookwoori.core.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClimbingMemberRepository extends JpaRepository<ClimbingMember, Long> {

    Optional<ClimbingMember> findByMemberAndClimbing(Member member, Climbing climbing);

    boolean existsByMemberAndClimbing(Member member, Climbing climbing);

    int countByClimbing(Climbing climbing);

    @Query("SELECT cm FROM ClimbingMember cm JOIN FETCH cm.member WHERE cm.climbing = :climbing")
    List<ClimbingMember> findByClimbingWithMember(@Param("climbing") Climbing climbing);

    Optional<ClimbingMember> findByMemberAndClimbing_ClimbingId(Member member, Long climbingId);

    @Query("SELECT cm.member.memberId FROM ClimbingMember cm WHERE cm.climbing = :climbing AND cm.hasShared = true")
    List<Long> findSharedMemberIdsByClimbing(@Param("climbing") Climbing climbing);

    @Query("SELECT cm FROM ClimbingMember cm JOIN FETCH cm.member m " +
        "WHERE cm.climbing.climbingId = :climbingId AND m.memberId = :memberId")
    ClimbingMember findClimbingMemberWithMember(@Param("climbingId") Long climbingId,
        @Param("memberId") Long memberId);

    @Query("SELECT cm.climbingMemberId FROM ClimbingMember cm WHERE cm.climbing = :climbing AND cm.hasShared = true ")
    List<Long> findSharedClimbingMemberIdsByClimbing(Climbing climbing);

    boolean existsByMemberAndClimbing_ClimbingId(Member member, Long climbingId);
}
