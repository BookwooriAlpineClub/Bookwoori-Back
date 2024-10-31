package org.bookwoori.core.domain.server.dto.response;

import java.util.List;

public record ServerListResponseDto(
    List<ServerItemDto> servers
) {

}
