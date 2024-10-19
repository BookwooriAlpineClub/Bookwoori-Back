package org.bookwoori.core.domain.server.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bookwoori.core.domain.server.entity.Server;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class ServerCreateRequestDto {

    @NotNull
    @Size(max = 20)
    private String name;
    private MultipartFile serverImg;
    private String description;

    public Server toEntity(String url) {
        return Server.builder()
            .name(this.name)
            .serverImg(url)
            .description(this.description)
            .build();
    }
}
