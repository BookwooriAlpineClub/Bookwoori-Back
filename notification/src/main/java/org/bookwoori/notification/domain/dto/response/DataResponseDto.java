package org.bookwoori.notification.domain.dto.response;

import lombok.Builder;

@Builder
public record DataResponseDto<T>(
        Boolean isSuccess,
        int code,
        String message,
        T result
) {
}