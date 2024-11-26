package org.bookwoori.notification.dto.response;

public record DataResponseDto<T>(
        CommonResponseDto commonResponseDto,
        T result
) {
}