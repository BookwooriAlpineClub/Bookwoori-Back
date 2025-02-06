package org.bookwoori.core.domain.server;

import lombok.Builder;
import org.bookwoori.core.domain.server.entity.Server;

@Builder
public record ServerIdResponseDto (
        Long serverId
) {
    public static ServerIdResponseDto from(Server server) {
        return ServerIdResponseDto.builder()
                .serverId(server.getServerId())
                .build();
    }
}
