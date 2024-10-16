package org.bookwoori.core.domain.server.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.bookwoori.core.domain.server.entity.Server;

@Getter
public class ServerCreateRequestDto {

    @NotBlank
    @Size(max = 20)
    private String name;
    private String serverImg;
    private String description;

    public Server toEntity() {
        return Server.builder()
            .name(this.name)
            .serverImg(this.serverImg)
            .description(this.description)
            .build();
    }
}
