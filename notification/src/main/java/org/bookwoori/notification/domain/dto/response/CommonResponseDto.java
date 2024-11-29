package org.bookwoori.notification.domain.dto.response;

import lombok.Builder;

@Builder
public record CommonResponseDto(
        Boolean isSuccess,
        int code,
        String message
) {


}

