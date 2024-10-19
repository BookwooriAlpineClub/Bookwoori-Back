package org.bookwoori.core.domain.server.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.bookwoori.core.domain.server.entity.Server;

@Getter
@AllArgsConstructor
@Builder
public class ServerResponseDto {

    private String name;
    private String serverImg;
    private String owner;
    private int memberCount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
    private String description;

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
