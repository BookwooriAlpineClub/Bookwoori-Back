package org.bookwoori.core.domain.server.dto.response;


import lombok.Builder;
import org.bookwoori.core.domain.server.entity.Server;

@Builder
public record ServerCreateResponseDto(
    Long serverId
){
    public static ServerCreateResponseDto from(Server server){
        return ServerCreateResponseDto.builder()
            .serverId(server.getServerId())
            .build();
    }
}
