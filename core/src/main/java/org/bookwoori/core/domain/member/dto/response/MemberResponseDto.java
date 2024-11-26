package org.bookwoori.core.domain.member.dto.response;

import lombok.Builder;
import org.bookwoori.core.domain.member.entity.Member;

@Builder
public record MemberProfileResponseDto(
    String nickname,
    String profileImg,
    String backgroundImg,
    int level,
    String mountain,
    int height,
    int totalPage) {

    public static MemberProfileResponseDto from(Member member) {
        return MemberProfileResponseDto.builder()
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
