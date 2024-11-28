package org.bookwoori.core.domain.member.dto.request;


public record GetOrSaveMemberRequestDto(
    Long kakaoId,
    String nickname,
    String profileImg
) {

}

