package org.bookwoori.core.domain.serverMember.service;

import java.util.List;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.server.entity.Server;
import org.bookwoori.core.domain.serverMember.dto.ServerMemberDto;
import org.bookwoori.core.domain.serverMember.entity.ServerMember;
import org.bookwoori.core.domain.serverMember.entity.ServerRole;

public interface ServerMemberService {

    ServerMember save(ServerMember serverMember);

    boolean isJoined(Member member, Server server);

    boolean isOwner(Member member, Server server);

    Member getOwner(Server server);

    int getMemberCount(Server server);

    List<ServerMemberDto> getAllMembersByServer(Server server);

    List<Server> getServerListByMember(Member member);

    void delete(Server server, Member member);

    ServerMember getByMemberAndServer(Member member, Server server);

    void delegateServerRole(Server server, Member from, Member to);

    List<Server> getAllByMemberAndRole(Member currentMember, ServerRole serverRole);
}
