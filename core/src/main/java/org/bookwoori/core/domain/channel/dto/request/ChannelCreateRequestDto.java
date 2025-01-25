package org.bookwoori.core.domain.channel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bookwoori.core.domain.category.infrastructure.CategoryEntity;
import org.bookwoori.core.domain.channel.infrastructure.ChannelEntity;
import org.bookwoori.core.domain.channel.entity.ChannelType;

public record ChannelCreateRequestDto(
    @NotNull
    Long categoryId,
    @NotBlank
    String name,
    @NotNull
    ChannelType type
) {

    public ChannelEntity toEntity(CategoryEntity category) {
        return ChannelEntity.builder()
            .category(category)
            .name(this.name)
            .channelType(this.type)
            .build();
    }
}
