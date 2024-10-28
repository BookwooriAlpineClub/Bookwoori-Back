package org.bookwoori.core.domain.server.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import org.bookwoori.core.domain.server.entity.Server;

@Builder
public record ServerDetailsResponseDto(
    String name,
    String serverImg,
    String owner,
    int memberCount,
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDateTime createdAt,
    String description) {

    public static ServerDetailsResponseDto from(Server server, String owner, int memberCount) {
        return ServerDetailsResponseDto.builder()
            .name(server.getName())
            .serverImg(server.getServerImg())
            .owner(owner)
            .memberCount(memberCount)
            .createdAt(server.getCreatedAt())
            .description(server.getDescription())
            .build();
    }
}
