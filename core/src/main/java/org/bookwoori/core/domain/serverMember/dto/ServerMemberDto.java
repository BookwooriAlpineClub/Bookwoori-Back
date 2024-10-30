package org.bookwoori.core.domain.serverMember.dto;

import lombok.Builder;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.serverMember.entity.ServerRole;

@Builder
public record ServerMemberDto(
    Long memberId,
    String nickname,
    String profileImg,
    int level,
    String mountain,
    ServerRole role
) {

    public static ServerMemberDto from(Member member, ServerRole role) {
        return ServerMemberDto.builder()
            .memberId(member.getMemberId())
            .nickname(member.getNickname())
            .profileImg(member.getProfileImg())
            .level(member.getGrade().getLevel())
            .mountain(member.getGrade().getMountain())
            .role(role)
            .build();
    }
}
