package org.bookwoori.core.domain.channel.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;

public enum ChannelType {
    CHAT,
    VOICE
    ;

    @JsonCreator
    public static ChannelType from(String s) {
        try {
            return ChannelType.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }
    }
}
