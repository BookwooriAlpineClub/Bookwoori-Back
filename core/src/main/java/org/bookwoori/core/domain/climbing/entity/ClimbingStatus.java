package org.bookwoori.core.domain.climbing.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.bookwoori.core.domain.channel.entity.ChannelType;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;

public enum ClimbingStatus {
    READY,
    RUNNING,
    FINISHED,
    FAILED;

    @JsonCreator
    public static ClimbingStatus from(String s) {
        try {
            return ClimbingStatus.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }
    }
}
