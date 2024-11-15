package org.bookwoori.core.domain.reviewEmoji.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;

public enum EmojiType {
    GOOD,
    HEART,
    SMILE,
    CRY,
    THINK;

    @JsonCreator
    public static EmojiType from(String s) {
        try {
            return EmojiType.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }
    }

}
