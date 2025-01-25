package org.bookwoori.core.domain.member.dto.response;

import lombok.Builder;
import org.bookwoori.core.domain.member.infrastructure.MemberEntity;

@Builder
public record MemberProfileResponseDto(
    String nickname,
    String profileImg
) {

    public static MemberProfileResponseDto from(MemberEntity member) {
        return MemberProfileResponseDto.builder()
            .nickname(member.getNickname())
            .profileImg(member.getProfileImg())
            .build();
    }
}
