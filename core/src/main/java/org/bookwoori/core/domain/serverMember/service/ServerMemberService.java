package org.bookwoori.core.domain.serverMember.service;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.server.entity.Server;
import org.bookwoori.core.domain.serverMember.entity.ServerMember;
import org.bookwoori.core.domain.serverMember.entity.ServerRole;
import org.bookwoori.core.domain.serverMember.repository.ServerMemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServerMemberService {

    private final ServerMemberRepository serverMemberRepository;

    public void saveServerMember(Member member, Server server, ServerRole role){
        ServerMember serverMember = ServerMember.builder()
            .member(member)
            .server(server)
            .role(role)
            .build();
        serverMemberRepository.save(serverMember);
    }

}
