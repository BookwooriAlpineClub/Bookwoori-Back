package org.bookwoori.core.domain.serverMember.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.server.entity.Server;
import org.bookwoori.core.domain.serverMember.dto.ServerMemberDto;
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
    public boolean isJoined(Member member, Server server) {
        return serverMemberRepository.existsByMemberAndServer(member, server);
    }

    @Transactional(readOnly = true)
    public boolean isOwner(Member member, Server server) {
        return serverMemberRepository.existsByMemberAndServerAndRole(member, server);
    }

    @Transactional(readOnly = true)
    public Member getOwner(Server server) {
        return serverMemberRepository.findOwnerByServer(server)
            .orElseThrow(() -> new CustomException(ErrorCode.SERVER_OWNER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public int getMemberCount(Server server) {
        return serverMemberRepository.countByServer(server);
    }

    @Transactional(readOnly = true)
    public List<ServerMemberDto> getAllMembersByServer(Server server, Member currentMember) {
        return serverMemberRepository.findAllByServer(server).stream().map(
                serverMember -> ServerMemberDto.from(serverMember.getMember(), serverMember.getRole(),
                    serverMember.getMember().equals(currentMember)))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Server> getServerListByMember(Member member) {
        return serverMemberRepository.findAllByMember(member).stream()
            .map(ServerMember::getServer).toList();
    }

    public void deleteServerMember(Server server, Member member) {
        serverMemberRepository.deleteByServerAndMember(server, member);
    }

    public ServerMember getByMemberAndServer(Member member, Server server) {
        return serverMemberRepository.findByMemberAndServer(member, server)
            .orElseThrow(() -> new CustomException(ErrorCode.SERVER_MEMBER_NOT_FOUND));
    }

    @Transactional
    public void delegateServerRole(Server server, Member from, Member to) {
        ServerMember owner = getByMemberAndServer(from, server);

        if (!owner.getRole().equals(ServerRole.OWNER)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        ServerMember newOwner = getByMemberAndServer(to, server);
        owner.updateRole(ServerRole.MEMBER);
        newOwner.updateRole(ServerRole.OWNER);
    }

}
