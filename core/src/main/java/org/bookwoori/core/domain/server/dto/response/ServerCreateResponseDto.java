package org.bookwoori.core.domain.server.dto.response;


import lombok.Builder;
import org.bookwoori.core.domain.server.infrastructure.ServerEntity;

@Builder
public record ServerCreateResponseDto(
    Long serverId
) {

    public static ServerCreateResponseDto from(ServerEntity server) {
        return ServerCreateResponseDto.builder()
            .serverId(server.getServerId())
            .build();
    }
}
