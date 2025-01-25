package org.bookwoori.core.domain.climbing.infrastructure;

import java.util.List;
import org.bookwoori.core.domain.climbing.entity.ClimbingStatus;
import org.bookwoori.core.domain.member.infrastructure.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClimbingJpaRepository extends JpaRepository<ClimbingEntity, Long> {

    @Query("SELECT c FROM ClimbingEntity c JOIN ClimbingMemberEntity cm ON c = cm.climbing WHERE cm.member = :member AND c.server.serverId = :serverId ORDER BY c.climbingId DESC")
    List<ClimbingEntity> findMyClimbings(MemberEntity member, Long serverId);

    @Query("SELECT c FROM ClimbingEntity c WHERE c.status = 'READY' AND c.server.serverId = :serverId ORDER BY c.startDate ASC, c.climbingId DESC")
    List<ClimbingEntity> findReadyClimbings(Long serverId);

    List<ClimbingEntity> findByServer_ServerIdAndStatus(Long serverId,
        ClimbingStatus climbingStatus);

    List<ClimbingEntity> findByServer_ServerIdAndStatusIn(Long serverId,
        List<ClimbingStatus> statuses);
}
