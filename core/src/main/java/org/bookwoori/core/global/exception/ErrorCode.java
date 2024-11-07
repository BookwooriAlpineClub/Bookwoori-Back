package org.bookwoori.core.global.exception;

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
    INVALID_FILE_FORMAT(400, 1500, "잘못된 파일 형식입니다."),
    UNSUPPORTED_FILE_FORMAT(400, 1501, "지원하지 않는 파일 형식입니다."),
    FILE_UPLOAD_FAIL(500, 1502, "파일 업로드에 실패했습니다."),

    /*
     * 인증/인가 관련 오류
     * (2000 ~ 2999)
     */

    UNAUTHORIZED(401, 2000, "인증 정보가 누락되거나 잘못되었습니다."),
    ACCESS_DENIED(403, 2001, "접근 권한이 없습니다."),
    INVALID_JWT_SIGNATURE(401, 2003, "잘못된 JWT 서명입니다."),
    INVALID_TOKEN(401, 2100, "잘못된 토큰입니다."),
    NO_COOKIE(404, 2101, "쿠키가 존재하지 않습니다."),
    EXPIRED_ACCESS_TOKEN(401, 2300, "만료된 엑세스 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(401, 2301, "만료된 리프레쉬 토큰입니다."),

    /*
     * 리소스 관련 오류
     * (3000 ~ 3999)
     */

    // Member (3000 ~ 3099)
    MEMBER_NOT_FOUND(404, 3000, "사용자를 찾을 수 없습니다."),
    SERVER_OWNER_NOT_FOUND(404, 3001, "서버 주인을 찾을 수 없습니다."),
    MEMBER_INACTIVE(404, 3002, "이미 계정을 삭제한 멤버입니다."),

    // Server (3100 ~ 3199)
    SERVER_NOT_FOUND(404, 3100, "서버를 찾을 수 없습니다."),
    ALREADY_JOINED_SERVER(409, 3101, "이미 참여하고 있는 서버입니다."),

    // Category (3200 ~ 3299)
    CATEGORY_NOT_FOUND(404, 3200, "카테고리를 찾을 수 없습니다."),

    // Channel (3300 ~ 3399)
    CHANNEL_NOT_FOUND(404, 3300, "채널을 찾을 수 없습니다."),

    // Climbing (3400 ~ 3499)
    CLIMBING_NOT_FOUND(404, 3400, "클라이밍 채널을 찾을 수 없습니다."),
    ALREADY_JOINED_CLIMBING(409, 3401, "이미 참여하고 있는 클라이밍 채널입니다."),
    CLIMBING_NOT_READY(409, 3402, "모집 중인 클라이밍 채널만 편집할 수 있습니다."),
    OWNER_CANNOT_LEAVE(409, 3403, "OWNER는 클라이밍 채널을 떠날 수 없습니다."),
    CLIMBINGMEMBER_NOT_FOUND(404, 3404, "클라이밍 멤버를 찾을 수 없습니다."),
    CLIMBING_NOT_RUNNING(409, 3405, "진행 중인 클라이밍이 아닙니다."),

    // Book (3500 ~ 3599)
    BOOK_NOT_FOUND(404, 3500, "책을 찾을 수 없습니다."),

    // Record (3600 ~ 3699)

    ;


    private final int status;
    private final int code;
    private final String message;
}
