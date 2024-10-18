package org.bookwoori.core.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /*
     * 공통적으로 발생하는 오류
     * (1000 ~ 1999)
     */

    BAD_REQUEST(400, 1000, "요청의 형식이나 내용이 잘못되었습니다."),
    MISSING_PARAMETER(400, 1001, "필수 파라미터가 누락되었습니다."),
    INVALID_FILE_FORMAT(400, 1500, "잘못된 파일 형식입니다."),
    UNSUPPORTED_FILE_FORMAT(400, 1501, "지원하지 않는 파일 형식입니다."),
    FILE_UPLOAD_FAIL(500,1502,"파일 업로드에 실패했습니다."),

    /*
     * 인증/인가 관련 오류
     * (2000 ~ 2999)
     */

    UNAUTHORIZED(401, 2000, "인증 정보가 누락되거나 잘못되었습니다."),
    ACCESS_DENIED(403, 2001, "접근 권한이 없습니다."),
    EXPIRED_ACCESS_TOKEN(401, 2200, "만료된 액세스 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(401, 2300, "만료된 리프레쉬 토큰입니다."),

    /*
     * 리소스 관련 오류
     * (3000 ~ 3999)
     */

    // Member (3000 ~ 3099)
    MEMBER_NOT_FOUND(404, 3000, "사용자를 찾을 수 없습니다."),
    SERVER_OWNER_NOT_FOUND(404, 3001, "서버 주인을 찾을 수 없습니다."),

    // Server (3100 ~ 3199)
    SERVER_NOT_FOUND(404, 3100, "서버를 찾을 수 없습니다.")

    // Category (3200 ~ 3299)

    // Channel (3300 ~ 3399)

    // Climbing (3400 ~ 3499)

    // Book (3500 ~ 3599)

    // Record (3600 ~ 3699)

    // Review (3700 ~ 3799)

    ;

    private final int status;
    private final int code;
    private final String message;
}
