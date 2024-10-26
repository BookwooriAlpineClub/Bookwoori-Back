package org.bookwoori.core.domain.member.facade;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.member.service.MemberService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AuthFacade {
    private final MemberService memberService;

    public void deleteMember(){
        Member currentMember = memberService.getCurrentMember();
        currentMember.deleteMember();
    }

    @Transactional(readOnly = true)
    public void getMemberStatus(Long kakaoId){
        memberService.getMemberStatus(kakaoId);
    }
}
