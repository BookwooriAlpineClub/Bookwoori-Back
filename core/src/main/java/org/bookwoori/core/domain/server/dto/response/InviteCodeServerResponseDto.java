package org.bookwoori.core.domain.server.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import org.bookwoori.core.domain.server.entity.Server;

@Builder
public record InviteCodeServerResponseDto(
    String name,
    Long serverId,
    String serverImg,
    String ownerNickname,
    int memberCount,
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDateTime createdAt,
    String description
) {

    public static InviteCodeServerResponseDto from(Server server, String nickname,
        int memberCount) {
        return InviteCodeServerResponseDto.builder()
            .name(server.getName())
            .serverId(server.getServerId())
            .serverImg(server.getServerImg())
            .ownerNickname(nickname)
            .memberCount(memberCount)
            .createdAt(server.getCreatedAt())
            .description(server.getDescription())
            .build();
    }
}
