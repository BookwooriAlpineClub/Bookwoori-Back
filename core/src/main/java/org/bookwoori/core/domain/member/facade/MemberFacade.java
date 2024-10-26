package org.bookwoori.core.domain.member.facade;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.member.dto.request.UpdateMemberRequestDto;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.member.service.MemberService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberFacade {
    private final MemberService memberService;

    public void updateMember(UpdateMemberRequestDto requestDto) {
        Member currentMember = memberService.getCurrentMember();
        currentMember.updateMember(requestDto.nickname(), requestDto.profileImg());
        memberService.saveMember(currentMember);
    }
}
