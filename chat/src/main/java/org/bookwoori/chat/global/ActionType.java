package org.bookwoori.chat.global;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.bookwoori.chat.global.exception.CustomException;
import org.bookwoori.chat.global.exception.ErrorCode;

public enum ActionType {
    ADD, REMOVE;

    @JsonCreator
    public static ActionType from(String s) {
        try {
            return ActionType.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }
    }
}