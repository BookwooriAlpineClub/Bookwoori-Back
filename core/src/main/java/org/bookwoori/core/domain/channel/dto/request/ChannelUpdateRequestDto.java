package org.bookwoori.core.domain.channel.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChannelUpdateRequestDto(
    @NotBlank
    String name
) {

}
