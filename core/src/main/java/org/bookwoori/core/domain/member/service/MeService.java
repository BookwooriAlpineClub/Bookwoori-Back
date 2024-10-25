package org.bookwoori.core.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookwoori.core.domain.member.dto.request.UpdateMemberRequestDto;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MeService {
    private final MemberService memberService;

    public void updateMember(UpdateMemberRequestDto requestDto) {
        Member currentMember = memberService.getCurrentMember();
        currentMember.updateMember(requestDto.nickname(), requestDto.profileImg());
        memberService.saveMember(currentMember);
    }
}
