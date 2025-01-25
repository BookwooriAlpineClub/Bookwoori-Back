package org.bookwoori.core.domain.server.dto.response;

import lombok.Builder;
import org.bookwoori.core.domain.server.infrastructure.ServerEntity;

@Builder
public record ServerItemDto(
    Long serverId,
    String name,
    String serverImg
) {

    public static ServerItemDto from(ServerEntity server) {
        return ServerItemDto.builder()
            .serverId(server.getServerId())
            .name(server.getName())
            .serverImg(server.getServerImg())
            .build();
    }
}
