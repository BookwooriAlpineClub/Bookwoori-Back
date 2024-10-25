package org.bookwoori.core.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.member.entity.Status;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
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
