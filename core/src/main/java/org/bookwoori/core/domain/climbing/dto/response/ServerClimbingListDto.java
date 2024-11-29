package org.bookwoori.core.domain.climbing.dto.response;

import java.util.List;

public record ServerClimbingListDto(
    List<ClimbingUnitDto> myClimbings,
    List<ClimbingUnitDto> readyClimbings,
    List<ClimbingUnitDto> runningClimbings,
    List<ClimbingUnitDto> endClimbingings) {

    public record ClimbingUnitDto(
        Long climbingId,
        String name,
        String cover) {

    }
}
