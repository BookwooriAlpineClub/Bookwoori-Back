package org.bookwoori.core.domain.member.dto.response;

import lombok.Builder;
import org.bookwoori.core.domain.member.entity.Member;

@Builder
public record MemberProfileResponseDto(
    boolean isMine,
    String nickname,
    String profileImg,
    int level,
    String mountain,
    int height,
    int totalPage) {

    public static MemberProfileResponseDto from(Member member, boolean isMine) {
        return MemberProfileResponseDto.builder()
            .isMine(isMine)
            .nickname(member.getNickname())
            .profileImg(member.getProfileImg())
            .level(member.getGrade().getLevel())
            .mountain(member.getGrade().getMountain())
            .height(member.getGrade().getHeight())
            .totalPage(member.getTotalPage())
            .build();
    }
}
