package org.bookwoori.gateway.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNAUTHORIZED(401, 2000, "인증 정보가 누락되었습니다."),
    INVALID_TOKEN(401, 2001, "잘못된 토큰입니다."),
    INVALID_JWT_SIGNATURE(401, 2002, "잘못된 JWT 서명입니다.");

    private final int status;
    private final int code;
    private final String message;
}

