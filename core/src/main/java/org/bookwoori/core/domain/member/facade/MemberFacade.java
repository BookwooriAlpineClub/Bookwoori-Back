package org.bookwoori.core.domain.member.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookwoori.core.domain.member.dto.request.UpdateMemberRequestDto;
import org.bookwoori.core.domain.member.dto.response.MemberLibraryResponseDto;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.member.service.MemberService;
import org.bookwoori.core.global.s3.S3Util;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class MemberFacade {

    private final MemberService memberService;
    private final S3Util s3Util;

    public void updateMember(UpdateMemberRequestDto requestDto) {
        Member currentMember = memberService.getCurrentMember();
        String profileImgUrl = s3Util.uploadImage(requestDto.profileImg(), "member/profile-image");
        currentMember.updateMember(requestDto.nickname(), profileImgUrl);
        memberService.saveMember(currentMember);
    }

    @Transactional(readOnly = true)
    public MemberLibraryResponseDto getMemberLibrary(Long memberId) {
        Member member = memberService.getMemberById(memberId);
        Member currentMember = memberService.getCurrentMember();
        boolean isMine = member.equals(currentMember);
        return MemberLibraryResponseDto.from(member, isMine);
    }
}
