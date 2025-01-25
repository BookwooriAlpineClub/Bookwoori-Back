package org.bookwoori.core.domain.member.facade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookwoori.core.domain.member.dto.request.GetOrSaveMemberRequestDto;
import org.bookwoori.core.domain.member.dto.response.GetMemberResponseDto;
import org.bookwoori.core.domain.member.infrastructure.MemberEntity;
import org.bookwoori.core.domain.member.service.MemberServiceImpl;
import org.bookwoori.core.domain.server.infrastructure.ServerEntity;
import org.bookwoori.core.domain.serverMember.entity.ServerRole;
import org.bookwoori.core.domain.serverMember.service.ServerMemberServiceImpl;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.bookwoori.core.global.s3.S3Util;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Component
public class AuthFacade {

    private final MemberServiceImpl memberService;
    private final ServerMemberServiceImpl serverMemberService;
    private final S3Util s3Util;

    public void deleteMember() {
        MemberEntity currentMember = memberService.getCurrentMember();
        s3Util.deleteImage(currentMember.getProfileImg());
        s3Util.deleteImage(currentMember.getBackgroundImg());
        List<ServerEntity> ownedServers = serverMemberService.getAllByMemberAndRole(currentMember,
            ServerRole.OWNER);
        if (!ownedServers.isEmpty()) {
            throw new CustomException(ErrorCode.DELEGATION_REQUIRED);
        }
        currentMember.deleteMember();
    }

    public GetMemberResponseDto getOrSaveMemberByKakaoId(GetOrSaveMemberRequestDto requestDto) {
        boolean isMember = memberService.existsByKakaoId(requestDto.kakaoId());
        if (isMember) {
            MemberEntity member = memberService.getMemberByKakaoId(requestDto.kakaoId());
            return GetMemberResponseDto.from(member);
        } else {
            MemberEntity member = MemberEntity.builder()
                .kakaoId(requestDto.kakaoId())
                .nickname(requestDto.nickname())
                .profileImg(requestDto.profileImg())
                .build();
            memberService.saveMember(member);
            return GetMemberResponseDto.from(member);
        }
    }
}
