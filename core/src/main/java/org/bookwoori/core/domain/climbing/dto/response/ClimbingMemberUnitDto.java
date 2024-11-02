package org.bookwoori.core.domain.climbing.dto.response;

import org.bookwoori.core.domain.climbingMember.entity.ClimbingMember;
import org.bookwoori.core.domain.record.entity.ReadingStatus;

public record ClimbingMemberUnitDto(
    Long memberId,
    String profileImg,
    int level,
    String mountain,
    ReadingStatus status,
    int currentPage,
    String memo
) {

    public static ClimbingMemberUnitDto from(ClimbingMember member, ReadingStatus status,
        int currentPage) {
        return new ClimbingMemberUnitDto(
            member.getMember().getMemberId(),
            member.getMember().getProfileImg(),
            member.getMember().getGrade().getLevel(),
            member.getMember().getGrade().getMountain(),
            status,
            currentPage,
            member.getMemo()
        );
    }
}
