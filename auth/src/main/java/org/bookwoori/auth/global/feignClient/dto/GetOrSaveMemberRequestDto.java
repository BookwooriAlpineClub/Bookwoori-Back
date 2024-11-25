package org.bookwoori.auth.global.feignClient.dto;

public record GetOrSaveMemberRequestDto(
    Long kakaoId,
    String nickname,
    String profileImg
) {

}

