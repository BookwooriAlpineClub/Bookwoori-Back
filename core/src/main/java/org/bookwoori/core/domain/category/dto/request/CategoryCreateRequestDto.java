package org.bookwoori.core.domain.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bookwoori.core.domain.category.infrastructure.CategoryEntity;
import org.bookwoori.core.domain.server.infrastructure.ServerEntity;

public record CategoryCreateRequestDto(
    @NotNull
    Long serverId,
    @NotBlank
    String name
) {

    public CategoryEntity toEntity(ServerEntity server) {
        return CategoryEntity.builder()
            .server(server)
            .name(this.name)
            .build();
    }
}
