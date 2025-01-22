package org.bookwoori.chat.domain.channelMessage.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import org.bookwoori.chat.domain.channelMessage.entity.ChannelMessage;

@Builder
public record ChannelMessageModifyResponseDto(
    String id,
    String content,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime modifiedAt
) {

    public static ChannelMessageModifyResponseDto from(ChannelMessage channelMessage) {
        return ChannelMessageModifyResponseDto.builder()
            .id(channelMessage.getId())
            .content(channelMessage.getContent())
            .modifiedAt(channelMessage.getModifiedAt())
            .build();
    }
}
