package org.bookwoori.core.domain.server.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.bookwoori.core.domain.server.infrastructure.ServerEntity;
import org.springframework.web.multipart.MultipartFile;

public record ServerCreateRequestDto(
    @NotBlank
    @Size(max = 20)
    String name,
    MultipartFile serverImg,
    String description) {

    public ServerEntity toEntity(String url) {
        return ServerEntity.builder()
            .name(this.name)
            .serverImg(url)
            .description(this.description)
            .build();
    }
}
