package org.bookwoori.chat.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public record CommonResponse(
    EventType eventType,
    @JsonInclude(Include.NON_NULL)
    Long messageRoomId,
    @JsonInclude(Include.NON_NULL)
    Long channelId,
    Object payload
) {

    public static CommonResponse fromDirectMessage(EventType eventType, Long messageRoomId,
        Object payload) {
        return new CommonResponse(eventType, messageRoomId, null, payload);
    }

    public static CommonResponse fromChannelMessage(EventType eventType, Long channelId,
        Object payload) {
        return new CommonResponse(eventType, null, channelId, payload);
    }
}
