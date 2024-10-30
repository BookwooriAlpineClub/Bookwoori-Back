package org.bookwoori.core.domain.server.dto.response;

import java.util.List;
import org.bookwoori.core.domain.serverMember.dto.ServerMemberDto;

public record ServerMemberListResponseDto(
    List<ServerMemberDto> members
) {

}
