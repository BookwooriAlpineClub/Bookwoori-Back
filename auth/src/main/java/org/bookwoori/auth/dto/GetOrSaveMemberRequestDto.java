package org.bookwoori.auth.dto;

public record GetOrSaveMemberRequestDto(
    Long kakaoId,
    String nickname,
    String profileImg
) {

}

