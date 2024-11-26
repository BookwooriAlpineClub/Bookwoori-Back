package org.bookwoori.auth.global.feignClient.dto;

public record GetMemberResponseDto(
    Long memberId,
    Long kakaoId,
    String nickname,
    String profileImg
) {
}
