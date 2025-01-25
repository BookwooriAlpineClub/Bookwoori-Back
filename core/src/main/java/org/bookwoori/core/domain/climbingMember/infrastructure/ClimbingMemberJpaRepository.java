package org.bookwoori.core.domain.climbingMember.infrastructure;

import java.util.List;
import java.util.Optional;
import org.bookwoori.core.domain.climbing.infrastructure.ClimbingEntity;
import org.bookwoori.core.domain.member.infrastructure.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClimbingMemberJpaRepository extends JpaRepository<ClimbingMemberEntity, Long> {

    Optional<ClimbingMemberEntity> findByMemberAndClimbing(MemberEntity member,
        ClimbingEntity climbing);

    boolean existsByMemberAndClimbing(MemberEntity member, ClimbingEntity climbing);

    int countByClimbing(ClimbingEntity climbing);

    @Query("SELECT cm FROM ClimbingMemberEntity cm JOIN FETCH cm.member WHERE cm.climbing = :climbing")
    List<ClimbingMemberEntity> findByClimbingWithMember(@Param("climbing") ClimbingEntity climbing);

    Optional<ClimbingMemberEntity> findByMemberAndClimbing_ClimbingId(MemberEntity member,
        Long climbingId);

    @Query("SELECT cm.member.memberId FROM ClimbingMemberEntity cm WHERE cm.climbing = :climbing AND cm.hasShared = true")
    List<Long> findSharedMemberIdsByClimbing(@Param("climbing") ClimbingEntity climbing);

    @Query("SELECT cm FROM ClimbingMemberEntity cm JOIN FETCH cm.member m " +
        "WHERE cm.climbing.climbingId = :climbingId AND m.memberId = :memberId")
    ClimbingMemberEntity findClimbingMemberWithMember(@Param("climbingId") Long climbingId,
        @Param("memberId") Long memberId);
}
