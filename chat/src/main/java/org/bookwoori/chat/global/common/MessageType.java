package org.bookwoori.chat.global.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.bookwoori.chat.global.exception.CustomException;
import org.bookwoori.chat.global.exception.ErrorCode;

public enum MessageType {
    TALK, FILE;


    @JsonCreator
    public static MessageType from(String s) {
        try {
            return MessageType.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }
    }
}
