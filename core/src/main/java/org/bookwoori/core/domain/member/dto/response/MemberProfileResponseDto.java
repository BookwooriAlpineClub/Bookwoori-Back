package org.bookwoori.core.domain.member.dto.response;

import lombok.Builder;
import org.bookwoori.core.domain.member.entity.Member;

@Builder
public record MemberProfileResponseDto(
    String nickname,
    String profileImg
) {

    public static MemberProfileResponseDto from(Member member) {
        return MemberProfileResponseDto.builder()
            .nickname(member.getNickname())
            .profileImg(member.getProfileImg())
            .build();
    }
}
