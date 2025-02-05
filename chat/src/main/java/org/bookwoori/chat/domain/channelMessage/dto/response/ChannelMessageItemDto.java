package org.bookwoori.chat.domain.channelMessage.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;
import org.bookwoori.chat.domain.channelMessage.entity.ChannelMessage;
import org.bookwoori.chat.global.common.EmojiType;
import org.bookwoori.chat.global.common.dto.EmojiDetailsDto;

@Builder
public record ChannelMessageItemDto(
    @JsonInclude(Include.NON_EMPTY)
    String parentId,
    @JsonInclude(Include.NON_EMPTY)
    Long parentMemberId,
    @JsonInclude(Include.NON_EMPTY)
    String parentContent,
    String id,
    Long channelId,
    Long memberId,
    String content,
    LocalDateTime createdAt,
    Map<EmojiType, EmojiDetailsDto> reactions
) {

    public static ChannelMessageItemDto from(ChannelMessage channelMessage) {
        return ChannelMessageItemDto.builder()
            .parentId(channelMessage.getParentId())
            .parentMemberId(channelMessage.getParentMemberId())
            .parentContent(channelMessage.getParentContent())
            .id(channelMessage.getId())
            .channelId(channelMessage.getChannelId())
            .memberId(channelMessage.getMemberId())
            .content(channelMessage.getContent())
            .createdAt(channelMessage.getCreatedAt())
            .reactions(mapReactions(channelMessage))
            .build();
    }

    private static Map<EmojiType, EmojiDetailsDto> mapReactions(ChannelMessage channelMessage) {
        return channelMessage.getReactions().entrySet().stream().collect(
            Collectors.toMap(Map.Entry::getKey, entry -> EmojiDetailsDto.from(entry.getValue())));
    }

}
