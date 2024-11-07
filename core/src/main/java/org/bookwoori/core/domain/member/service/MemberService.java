package org.bookwoori.core.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.member.entity.Status;
import org.bookwoori.core.domain.member.repository.MemberRepository;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Member getCurrentMember() throws CustomException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByKakaoId(Long.valueOf(authentication.getName()))
            .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));
        return member;
    }

    public void getMemberStatus(Long kakaoId) {
        memberRepository.findByKakaoId(kakaoId)
            .filter(member -> member.getStatus() != Status.INACTIVE)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_INACTIVE));
    }

    public void saveMember(Member member) {
        memberRepository.save(member);
    }
}
