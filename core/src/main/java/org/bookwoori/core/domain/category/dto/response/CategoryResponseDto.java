package org.bookwoori.core.domain.category.dto.response;

import java.util.List;
import lombok.Builder;
import org.bookwoori.core.domain.category.infrastructure.CategoryEntity;
import org.bookwoori.core.domain.channel.dto.response.ChannelResponseDto;

@Builder
public record CategoryResponseDto(
    Long categoryId,
    String name,
    List<ChannelResponseDto> channels
) {

    public static CategoryResponseDto from(CategoryEntity category,
        List<ChannelResponseDto> channels) {
        return CategoryResponseDto.builder()
            .categoryId(category.getCategoryId())
            .name(category.getName())
            .channels(channels)
            .build();
    }
}
