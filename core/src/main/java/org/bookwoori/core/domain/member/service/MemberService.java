package org.bookwoori.core.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.member.entity.Status;
import org.bookwoori.core.domain.member.repository.MemberRepository;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
        if (authentication == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        try {
            String memberIdString = (String) authentication.getPrincipal();
            Long memberId = Long.valueOf(memberIdString);
            return getMemberById(memberId);
        } catch (NumberFormatException e) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }
    
    @Transactional(readOnly = true)
    public Member getMemberByKakaoId(Long kakaoId) {
        Member member = memberRepository.findByKakaoId(kakaoId)
            .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));
        return member;
    }

    @Transactional(readOnly = true)
    public void validateMemberStatus(Long kakaoId) {
        memberRepository.findByKakaoId(kakaoId)
            .filter(member -> member.getStatus() != Status.INACTIVE)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_INACTIVE));
    }

    public void saveMember(Member member) {
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public boolean existsByKakaoId(Long kakaoId) {
        return memberRepository.existsByKakaoId(kakaoId);
    }
}
