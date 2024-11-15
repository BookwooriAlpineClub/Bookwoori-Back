package org.bookwoori.core.domain.server.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import org.bookwoori.core.domain.server.entity.Server;

@Builder
public record ServerDetailsResponseDto(
    String name,
    String serverImg,
    String ownerNickname,
    int memberCount,
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDateTime createdAt,
    String description,

    boolean isOwner
) {

    public static ServerDetailsResponseDto from(Server server, String nickname, int memberCount,
        boolean isOwner) {
        return ServerDetailsResponseDto.builder()
            .name(server.getName())
            .serverImg(server.getServerImg())
            .ownerNickname(nickname)
            .memberCount(memberCount)
            .createdAt(server.getCreatedAt())
            .description(server.getDescription())
            .isOwner(isOwner)
            .build();
    }
}
