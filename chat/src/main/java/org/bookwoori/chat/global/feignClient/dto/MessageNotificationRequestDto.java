package org.bookwoori.chat.global.feignClient.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.bookwoori.chat.domain.channelMessage.entity.ChannelMessage;
import org.bookwoori.chat.domain.directMessage.entity.DirectMessage;

@Builder
public record MessageNotificationRequestDto(
    @NotNull
    Long memberId,
    @NotNull
    String nickname,
    @NotNull
    String type,
    @NotNull
    String content,
    @JsonInclude(Include.NON_NULL)
    Long roomId,
    @JsonInclude(Include.NON_NULL)
    Long channelId,
    @NotNull
    String target
) {

    public static MessageNotificationRequestDto from(DirectMessage directMessage) {
        return MessageNotificationRequestDto.builder()
            .memberId(directMessage.getMemberId())
            .nickname(directMessage.getNickname())
            .type("directMessage")
            .content(directMessage.getContent())
            .roomId(directMessage.getMessageRoomId())
            .target(directMessage.getTarget().toString())
            .build();
    }

    public static MessageNotificationRequestDto from(ChannelMessage channelMessage) {
        return MessageNotificationRequestDto.builder()
            .memberId(channelMessage.getMemberId())
            .nickname(channelMessage.getNickname())
            .type("channelMessage")
            .content(channelMessage.getContent())
            .channelId(channelMessage.getChannelId())
            .target(channelMessage.getTarget().toString())
            .build();
    }
}
