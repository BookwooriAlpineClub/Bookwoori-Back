package org.bookwoori.core.domain.climbing.repository;

import java.util.List;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClimbingRepository extends JpaRepository<Climbing, Long> {

    @Query("SELECT c FROM Climbing c JOIN ClimbingMember cm ON c = cm.climbing WHERE cm.member = :member AND c.server.serverId = :serverId ORDER BY c.climbingId DESC")
    List<Climbing> findMyClimbings(Member member, Long serverId);

    @Query("SELECT c FROM Climbing c WHERE c.status = 'READY' AND c.server.serverId = :serverId ORDER BY c.startDate ASC, c.climbingId DESC")
    List<Climbing> findReadyClimbings(Long serverId);
}
