package org.bookwoori.notification.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommonResponseDto {

    protected Boolean isSuccess;
    protected int code;
    protected String message;
}

