package org.bookwoori.core.domain.serverMember.infrastructure;

import java.util.List;
import java.util.Optional;
import org.bookwoori.core.domain.member.infrastructure.MemberEntity;
import org.bookwoori.core.domain.server.infrastructure.ServerEntity;
import org.bookwoori.core.domain.serverMember.entity.ServerRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ServerMemberJpaRepository extends JpaRepository<ServerMemberEntity, Long> {

    int countByServer(ServerEntity server);

    @Query("SELECT COUNT(sm) > 0 FROM ServerMemberEntity sm WHERE sm.member = :member AND sm.server = :server")
    boolean existsByMemberAndServer(MemberEntity member, ServerEntity server);

    @Query("SELECT COUNT(sm) > 0 FROM ServerMemberEntity sm WHERE sm.member = :member AND sm.server = :server AND sm.role = 'OWNER'")
    boolean existsByMemberAndServerAndRole(MemberEntity member, ServerEntity server);

    @Query("SELECT m.member FROM ServerMemberEntity m WHERE m.server = :server AND m.role = 'OWNER'")
    Optional<MemberEntity> findOwnerByServer(@Param("server") ServerEntity server);

    @Query("SELECT sm FROM ServerMemberEntity sm JOIN FETCH sm.member WHERE sm.server = :server")
    List<ServerMemberEntity> findAllByServer(ServerEntity server);

    Optional<ServerMemberEntity> findByMemberAndServer(MemberEntity member, ServerEntity server);


    @Query("SELECT sm FROM ServerMemberEntity sm JOIN FETCH sm.server WHERE sm.member = :member")
    List<ServerMemberEntity> findAllByMember(MemberEntity member);

    void deleteByServerAndMember(ServerEntity server, MemberEntity member);

    @Query("SELECT sm.server FROM ServerMemberEntity sm WHERE sm.member = :member AND sm.role = :role")
    List<ServerEntity> findByMemberAndRole(@Param("member") MemberEntity member,
        @Param("role") ServerRole role);
}
