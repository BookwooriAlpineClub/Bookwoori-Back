package org.bookwoori.core.domain.serverMember.repository;

import java.util.List;
import java.util.Optional;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.server.entity.Server;
import org.bookwoori.core.domain.serverMember.entity.ServerMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ServerMemberRepository extends JpaRepository<ServerMember, Long> {

    int countByServer(Server server);
    @Query("SELECT m.member FROM ServerMember m WHERE m.server = :server AND m.role = 'OWNER'")
    Optional<Member> findOwnerByServer(@Param("server") Server server);

    @Query("SELECT sm FROM ServerMember sm JOIN FETCH sm.member WHERE sm.server = :server")
    List<ServerMember> findAllByServer(Server server);
}
