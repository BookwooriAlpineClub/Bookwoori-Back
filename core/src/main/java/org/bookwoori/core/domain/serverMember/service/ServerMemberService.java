package org.bookwoori.core.domain.serverMember.service;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.server.entity.Server;
import org.bookwoori.core.domain.serverMember.entity.ServerMember;
import org.bookwoori.core.domain.serverMember.entity.ServerRole;
import org.bookwoori.core.domain.serverMember.repository.ServerMemberRepository;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ServerMemberService {

    private final ServerMemberRepository serverMemberRepository;

    public void saveServerMember(Member member, Server server, ServerRole role) {
        ServerMember serverMember = ServerMember.builder()
            .member(member)
            .server(server)
            .role(role)
            .build();
        serverMemberRepository.save(serverMember);
    }

    @Transactional(readOnly = true)
    public Member getOwner(Server server) {
        return serverMemberRepository.findOwnerByServer(server).orElseThrow(() -> {
            throw new CustomException(ErrorCode.SERVER_OWNER_NOT_FOUND);
        });
    }

    @Transactional(readOnly = true)
    public int getMemberCount(Server server) {
        return serverMemberRepository.countByServer(server);
    }

}
