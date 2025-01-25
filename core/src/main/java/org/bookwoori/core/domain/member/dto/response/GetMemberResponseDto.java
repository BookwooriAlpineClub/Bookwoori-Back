package org.bookwoori.core.domain.member.dto.response;

import lombok.Builder;
import org.bookwoori.core.domain.member.infrastructure.MemberEntity;

@Builder
public record GetMemberResponseDto(
    Long memberId,
    Long kakaoId,
    String nickname,
    String profileImg
) {

    public static GetMemberResponseDto from(MemberEntity member) {
        return GetMemberResponseDto.builder()
            .memberId(member.getMemberId())
            .kakaoId(member.getKakaoId())
            .nickname(member.getNickname())
            .build();
    }
}
