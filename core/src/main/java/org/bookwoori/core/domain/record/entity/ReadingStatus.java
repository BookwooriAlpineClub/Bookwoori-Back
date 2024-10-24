package org.bookwoori.core.domain.record.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.bookwoori.core.domain.member.entity.Grade;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;

public enum ReadingStatus {
    WISH,
    READING,
    FINISHED;

    @JsonCreator
    public static ReadingStatus from(String s) {
        try {
            return ReadingStatus.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }
    }
}
