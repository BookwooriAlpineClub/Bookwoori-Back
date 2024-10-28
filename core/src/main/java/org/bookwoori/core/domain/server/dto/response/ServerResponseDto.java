package org.bookwoori.core.domain.server.dto.response;

import lombok.Builder;
import org.bookwoori.core.domain.server.entity.Server;

@Builder
public record ServerResponseDto(
    Long serverId,
    String name,
    String serverImg
) {

    public static ServerResponseDto from(Server server) {
        return ServerResponseDto.builder()
            .serverId(server.getServerId())
            .name(server.getName())
            .serverImg(server.getServerImg())
            .build();
    }
}
