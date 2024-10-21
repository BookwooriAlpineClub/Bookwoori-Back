package org.bookwoori.core.domain.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bookwoori.core.domain.category.entity.Category;
import org.bookwoori.core.domain.server.entity.Server;

public record CategoryCreateRequestDto(
    @NotNull
    Long serverId,
    @NotBlank
    String name
) {

    public Category toEntity(Server server) {
        return Category.builder()
            .server(server)
            .name(this.name)
            .build();
    }
}
