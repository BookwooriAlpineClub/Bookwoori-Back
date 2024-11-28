package org.bookwoori.core.domain.member.dto.response;

import lombok.Builder;
import org.bookwoori.core.domain.member.entity.Member;

@Builder
public record MemberResponseDto(
    boolean isMine,
    String nickname,
    String profileImg,
    String backgroundImg,
    int level,
    String mountain,
    int height,
    int totalPage) {

    public static MemberResponseDto from(Member member, boolean isMine) {
        return MemberResponseDto.builder()
            .isMine(isMine)
            .nickname(member.getNickname())
            .profileImg(member.getProfileImg())
            .backgroundImg(member.getBackgroundImg())
            .level(member.getGrade().getLevel())
            .mountain(member.getGrade().getMountain())
            .height(member.getGrade().getHeight())
            .totalPage(member.getTotalPage())
            .build();
    }
}
