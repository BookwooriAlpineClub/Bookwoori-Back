package org.bookwoori.core.domain.climbing.dto.response;

import lombok.Builder;
import org.bookwoori.core.domain.member.entity.Member;

@Builder
public record ReviewEmojiMemberUnitDto(
    Long memberId,
    String nickname,
    String profileImg,
    int level,
    String mountain
) {

    public static ReviewEmojiMemberUnitDto from(Member member) {
        return ReviewEmojiMemberUnitDto.builder()
            .memberId(member.getMemberId())
            .nickname(member.getNickname())
            .profileImg(member.getProfileImg())
            .level(member.getGrade().getLevel())
            .mountain(member.getGrade().getMountain())
            .build();
    }

}
