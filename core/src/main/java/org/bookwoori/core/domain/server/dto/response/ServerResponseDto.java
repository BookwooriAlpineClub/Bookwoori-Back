package org.bookwoori.core.domain.server.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.bookwoori.core.domain.server.entity.Server;

@Builder
public record ServerResponseDto(
    String name,
    String serverImg,
    String owner,
    int memberCount,
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDateTime createdAt,
    String description) {

    public static ServerResponseDto from(Server server, String owner, int memberCount) {
        return ServerResponseDto.builder()
            .name(server.getName())
            .serverImg(server.getServerImg())
            .owner(owner)
            .memberCount(memberCount)
            .createdAt(server.getCreatedAt())
            .description(server.getDescription())
            .build();
    }
}
