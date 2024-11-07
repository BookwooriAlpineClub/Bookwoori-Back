package org.bookwoori.core.domain.climbing.dto.response;

import java.util.List;

public record ClimbingReviewListResponseDto(
    List<ClimbingMemberReviewUnitDto> ClimbingMemberReviewList
) {

}
