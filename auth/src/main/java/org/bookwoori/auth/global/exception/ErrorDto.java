package org.bookwoori.auth.global.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ErrorDto {

    private String timestamp;
    private int status;
    private int code;
    private String message;
    private String path;
}
