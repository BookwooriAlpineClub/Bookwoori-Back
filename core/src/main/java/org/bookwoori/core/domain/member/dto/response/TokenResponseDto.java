package org.bookwoori.core.domain.member.dto.response;

import lombok.Builder;

@Builder
public record TokenResponseDto(
    String accessToken
) {

}
