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
    double totalHeight,
    int totalPage) {

    public static MemberResponseDto from(Member member, boolean isMine) {
        return MemberResponseDto.builder()
            .isMine(isMine)
            .nickname(member.getNickname())
            .profileImg(member.getProfileImg())
            .backgroundImg(member.getBackgroundImg())
            .level(member.getGrade().getLevel())
            .mountain(member.getGrade().getMountain())
            .totalHeight(Math.round(member.getTotalHeight() * 100.0) / 100.0)
            .totalPage(member.getTotalPage())
            .build();
    }
}
