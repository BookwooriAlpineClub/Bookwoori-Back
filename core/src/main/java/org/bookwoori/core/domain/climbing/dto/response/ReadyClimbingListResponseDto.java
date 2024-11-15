package org.bookwoori.core.domain.climbing.dto.response;

import java.util.List;

public record ReadyClimbingListResponseDto(
    List<ClimbingDetailsResponseDto> readyClimbingList
) {

}
