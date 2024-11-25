package org.bookwoori.chat.global;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.bookwoori.chat.global.exception.CustomException;
import org.bookwoori.chat.global.exception.ErrorCode;

public enum EmojiType {
    THUMBS_UP, HEART_HANDS, SMILING_FACE, CRYING_FACE, THINKING_FACE;

    @JsonCreator
    public static EmojiType from(String s) {
        try {
            return EmojiType.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }
    }
}
