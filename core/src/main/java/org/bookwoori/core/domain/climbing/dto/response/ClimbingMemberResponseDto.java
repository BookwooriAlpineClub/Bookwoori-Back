package org.bookwoori.core.domain.climbing.dto.response;

import java.util.List;

public record ClimbingMemberResponseDto(
    List<ClimbingMemberUnitDto> climbingMemberList
) {

}
