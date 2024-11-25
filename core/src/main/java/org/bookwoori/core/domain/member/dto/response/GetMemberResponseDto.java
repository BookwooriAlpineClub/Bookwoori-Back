package org.bookwoori.core.domain.member.dto.response;

import lombok.Builder;
import org.bookwoori.core.domain.member.entity.Member;

@Builder
public record GetMemberResponseDto(
    Long memberId,
    Long kakaoId,
    String nickname,
    String profileImg
) {

    public static GetMemberResponseDto from(Member member) {
        return GetMemberResponseDto.builder()
            .memberId(member.getMemberId())
            .kakaoId(member.getKakaoId())
            .nickname(member.getNickname())
            .build();
    }
}
