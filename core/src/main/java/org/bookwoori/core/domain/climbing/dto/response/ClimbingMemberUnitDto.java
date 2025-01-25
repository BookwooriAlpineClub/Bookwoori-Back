package org.bookwoori.core.domain.climbing.dto.response;

import lombok.Builder;
import org.bookwoori.core.domain.climbingMember.infrastructure.ClimbingMemberEntity;
import org.bookwoori.core.domain.record.entity.ReadingStatus;

@Builder
public record ClimbingMemberUnitDto(
    boolean isMine,
    Long memberId,
    String nickname,
    String profileImg,
    int level,
    String mountain,
    ReadingStatus status,
    int currentPage,
    String memo
) {

    public static ClimbingMemberUnitDto from(boolean isMine, ClimbingMemberEntity member,
        ReadingStatus status,
        int currentPage) {
        return ClimbingMemberUnitDto.builder()
            .isMine(isMine)
            .memberId(member.getMember().getMemberId())
            .nickname(member.getMember().getNickname())
            .profileImg(member.getMember().getProfileImg())
            .level(member.getMember().getGrade().getLevel())
            .mountain(member.getMember().getGrade().getMountain())
            .status(status)
            .currentPage(currentPage)
            .memo(member.getMemo())
            .build();
    }
}
