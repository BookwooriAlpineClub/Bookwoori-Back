package org.bookwoori.core.domain.member.dto.response;

import lombok.Builder;
import org.bookwoori.core.domain.member.entity.Member;

@Builder
public record MemberLibraryResponseDto(
    boolean isMine,
    String nickname,
    int level,
    String mountain,
    int height,
    int totalPage) {

    public static MemberLibraryResponseDto from(Member member, boolean isMine) {
        return MemberLibraryResponseDto.builder()
            .isMine(isMine)
            .nickname(member.getNickname())
            .level(member.getGrade().getLevel())
            .mountain(member.getGrade().getMountain())
            .height(member.getGrade().getHeight())
            .totalPage(member.getTotalPage())
            .build();
    }
}
