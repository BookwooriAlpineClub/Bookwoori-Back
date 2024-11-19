package org.bookwoori.chat.global;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MessageType {
    TALK, FILE;


    @JsonCreator
    public static MessageType from(String s) {
        try {
            return MessageType.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("INVALID ENUM TYPE");
        }
    }
}
