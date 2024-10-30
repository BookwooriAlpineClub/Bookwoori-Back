package org.bookwoori.core.domain.channel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bookwoori.core.domain.category.entity.Category;
import org.bookwoori.core.domain.channel.entity.Channel;
import org.bookwoori.core.domain.channel.entity.ChannelType;

public record ChannelCreateRequestDto(
    @NotNull
    Long categoryId,
    @NotBlank
    String name,
    @NotNull
    ChannelType type
) {

    public Channel toEntity(Category category) {
        return Channel.builder()
            .category(category)
            .name(this.name)
            .channelType(this.type)
            .build();
    }
}
