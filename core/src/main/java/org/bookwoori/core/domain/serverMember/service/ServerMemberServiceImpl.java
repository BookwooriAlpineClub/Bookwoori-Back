package org.bookwoori.core.domain.serverMember.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.member.infrastructure.MemberEntity;
import org.bookwoori.core.domain.server.infrastructure.ServerEntity;
import org.bookwoori.core.domain.serverMember.dto.ServerMemberDto;
import org.bookwoori.core.domain.serverMember.entity.ServerRole;
import org.bookwoori.core.domain.serverMember.infrastructure.ServerMemberEntity;
import org.bookwoori.core.domain.serverMember.infrastructure.ServerMemberJpaRepository;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ServerMemberServiceImpl {

    private final ServerMemberJpaRepository serverMemberRepository;

    public void saveServerMember(MemberEntity member, ServerEntity server, ServerRole role) {
        ServerMemberEntity serverMember = ServerMemberEntity.builder()
            .member(member)
            .server(server)
            .role(role)
            .build();
        serverMemberRepository.save(serverMember);
    }

    @Transactional(readOnly = true)
    public boolean isJoined(MemberEntity member, ServerEntity server) {
        return serverMemberRepository.existsByMemberAndServer(member, server);
    }

    @Transactional(readOnly = true)
    public boolean isOwner(MemberEntity member, ServerEntity server) {
        return serverMemberRepository.existsByMemberAndServerAndRole(member, server);
    }

    @Transactional(readOnly = true)
    public MemberEntity getOwner(ServerEntity server) {
        return serverMemberRepository.findOwnerByServer(server)
            .orElseThrow(() -> new CustomException(ErrorCode.SERVER_OWNER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public int getMemberCount(ServerEntity server) {
        return serverMemberRepository.countByServer(server);
    }

    @Transactional(readOnly = true)
    public List<ServerMemberDto> getAllMembersByServer(ServerEntity server) {
        return serverMemberRepository.findAllByServer(server).stream().map(
                serverMember -> ServerMemberDto.from(serverMember.getMember(), serverMember.getRole()))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ServerEntity> getServerListByMember(MemberEntity member) {
        return serverMemberRepository.findAllByMember(member).stream()
            .map(ServerMemberEntity::getServer).toList();
    }

    public void deleteServerMember(ServerEntity server, MemberEntity member) {
        serverMemberRepository.deleteByServerAndMember(server, member);
    }

    public ServerMemberEntity getByMemberAndServer(MemberEntity member, ServerEntity server) {
        return serverMemberRepository.findByMemberAndServer(member, server)
            .orElseThrow(() -> new CustomException(ErrorCode.SERVER_MEMBER_NOT_FOUND));
    }

    @Transactional
    public void delegateServerRole(ServerEntity server, MemberEntity from, MemberEntity to) {
        ServerMemberEntity owner = getByMemberAndServer(from, server);

        if (!owner.getRole().equals(ServerRole.OWNER)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        ServerMemberEntity newOwner = getByMemberAndServer(to, server);
        owner.updateRole(ServerRole.MEMBER);
        newOwner.updateRole(ServerRole.OWNER);
    }

    @Transactional(readOnly = true)
    public List<ServerEntity> getAllByMemberAndRole(MemberEntity currentMember,
        ServerRole serverRole) {
        return serverMemberRepository.findByMemberAndRole(currentMember, serverRole);
    }
}
