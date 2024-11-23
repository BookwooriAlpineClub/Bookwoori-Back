package org.bookwoori.auth.dto;

public record GetMemberResponseDto(
    Long memberId,
    Long kakaoId,
    String nickname,
    String profileImg
) {
}
