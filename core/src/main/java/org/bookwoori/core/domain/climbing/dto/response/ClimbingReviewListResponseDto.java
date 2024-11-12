package org.bookwoori.core.domain.climbing.dto.response;

import java.util.List;

public record ClimbingReviewListResponseDto(
    boolean hasShared,
    List<ClimbingMemberReviewUnitDto> ClimbingMemberReviewList
) {

}
