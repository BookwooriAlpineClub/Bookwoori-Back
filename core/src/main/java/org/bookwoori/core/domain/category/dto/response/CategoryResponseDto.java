package org.bookwoori.core.domain.category.dto.response;

import java.util.List;
import lombok.Builder;
import org.bookwoori.core.domain.category.entity.Category;
import org.bookwoori.core.domain.channel.dto.response.ChannelResponseDto;

@Builder
public record CategoryResponseDto(
    Long categoryId,
    String name,
    List<ChannelResponseDto> channels
) {

    public static CategoryResponseDto from(Category category, List<ChannelResponseDto> channels) {
        return CategoryResponseDto.builder()
            .categoryId(category.getCategoryId())
            .name(category.getName())
            .channels(channels)
            .build();
    }
}
