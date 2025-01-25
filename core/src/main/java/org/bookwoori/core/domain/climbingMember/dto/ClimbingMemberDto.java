package org.bookwoori.core.domain.climbingMember.dto;

import lombok.Builder;
import org.bookwoori.core.domain.climbingMember.entity.ClimbingRole;
import org.bookwoori.core.domain.member.infrastructure.MemberEntity;

@Builder
public record ClimbingMemberDto(
    Long memberId,
    String nickname,
    String profileImg,
    int level,
    String mountain,
    ClimbingRole role,
    boolean hasShared,
    String memo
) {

    public static ClimbingMemberDto from(MemberEntity member, ClimbingRole role) {
        return ClimbingMemberDto.builder()
            .memberId(member.getMemberId())
            .nickname(member.getNickname())
            .profileImg(member.getProfileImg())
            .level(member.getGrade().getLevel())
            .mountain(member.getGrade().getMountain())
            .role(role)
            .hasShared(false)
            .memo(null)
            .build();
    }
}
