package org.bookwoori.chat.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /*
     * 공통적으로 발생하는 오류
     * (1000 ~ 1999)
     */

    BAD_REQUEST(400, 1000, "요청의 형식이나 내용이 잘못되었습니다."),
    MISSING_PARAMETER(400, 1001, "필수 파라미터가 누락되었습니다."),
    INVALID_ENUM_VALUE(400, 1002, "잘못된 ENUM 값입니다."),

    /*
     * 인증/인가 관련 오류
     * (2000 ~ 2999)
     */

    UNAUTHORIZED(401, 2000, "인증 정보가 누락되거나 잘못되었습니다."),
    ACCESS_DENIED(403, 2001, "접근 권한이 없습니다."),

    /*
     * 채팅 서버 오류
     * (5000 ~ 5999)
     */

    DIRECT_MESSAGE_NOT_FOUND(404, 5000, "해당 다이렉트 메시지를 찾을 수 없습니다."),
    CHANNEL_MESSAGE_NOT_FOUND(404, 5001, "해당 채널 메시지를 찾을 수 없습니다."),
    INVALID_MESSAGE_TYPE_EXCEPTION(400, 5500, "해당 타입의 메시지에 대해 요청을 처리할 수 없습니다.");


    private final int status;
    private final int code;
    private final String message;
}
