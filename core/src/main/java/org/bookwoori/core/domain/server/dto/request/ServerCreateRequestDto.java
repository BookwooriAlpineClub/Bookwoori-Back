package org.bookwoori.core.domain.server.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.bookwoori.core.domain.server.entity.Server;
import org.springframework.web.multipart.MultipartFile;

public record ServerCreateRequestDto(
    @NotBlank
    @Size(max = 20)
    String name,
    MultipartFile serverImg,
    String description) {

    public Server toEntity(String url) {
        return Server.builder()
            .name(this.name)
            .serverImg(url)
            .description(this.description)
            .build();
    }
}
