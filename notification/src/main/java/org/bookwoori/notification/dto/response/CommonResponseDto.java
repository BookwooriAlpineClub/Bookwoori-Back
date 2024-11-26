package org.bookwoori.notification.dto.response;

public record CommonResponseDto(
        Boolean isSuccess,
        int code,
        String message
) {


}

