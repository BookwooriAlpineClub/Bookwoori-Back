package org.bookwoori.chat.channelMessage.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import org.bookwoori.chat.channelMessage.domain.ChannelMessage;
import org.bookwoori.chat.global.common.MessageType;

@Builder
public record ChannelMessageReplyResponseDto(
    String id,
    Long channelId,
    Long memberId,
    MessageType type,
    String content,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt,
    String parentId,
    String parentContent
) {

    public static ChannelMessageReplyResponseDto from(ChannelMessage channelMessage) {
        return ChannelMessageReplyResponseDto.builder()
            .id(channelMessage.getId())
            .channelId(channelMessage.getChannelId())
            .memberId(channelMessage.getMemberId())
            .type(channelMessage.getType())
            .content(channelMessage.getContent())
            .createdAt(channelMessage.getCreatedAt())
            .parentId(channelMessage.getParentId())
            .parentContent(channelMessage.getParentContent())
            .build();
    }
}
