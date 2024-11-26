package org.bookwoori.core.domain.channel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChannelModifyRequestDto(
    @NotBlank
    String name,
    @NotNull
    Long categoryId
) {

}
