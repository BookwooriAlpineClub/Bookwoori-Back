package org.bookwoori.core.domain.member.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookwoori.core.domain.member.dto.request.UpdateMemberRequestDto;
import org.bookwoori.core.domain.member.dto.response.MemberResponseDto;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.member.service.MemberService;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.bookwoori.core.global.s3.S3Util;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class MemberFacade {

    private final MemberService memberService;

    private final S3Util s3Util;

    @Transactional(readOnly = true)
    public MemberResponseDto getMemberProfile(Long memberId) {
        Member member = memberService.getMemberById(memberId);
        Member currentMember = memberService.getCurrentMember();
        boolean isMine = member.equals(currentMember);
        return MemberResponseDto.from(member, isMine);
    }

    @Transactional(readOnly = true)
    public MemberResponseDto getMyProfile() {
        Member currentMember = memberService.getCurrentMember();
        return MemberResponseDto.from(currentMember, true);
    }

    public void updateMember(UpdateMemberRequestDto requestDto) {
        Member currentMember = memberService.getCurrentMember();
        String newNickname = requestDto.nickname();
        if (!currentMember.getNickname().equals(newNickname)) {
            if (memberService.existsByNickname(newNickname)) {
                throw new CustomException(ErrorCode.ALREADY_EXIST_NICKNAME);
            }
        }
        currentMember.updateMember(requestDto.nickname());
    }

    public void updateMemberProfileImg(MultipartFile newImage) {
        Member member = memberService.getCurrentMember();
        s3Util.deleteImage(member.getProfileImg());
        member.updateMemberProfileImg(s3Util.uploadImage(newImage, "member/profile-image"));
    }

    public void updateMemberBackgrounImg(MultipartFile newImage) {
        Member member = memberService.getCurrentMember();
        s3Util.deleteImage(member.getProfileImg());
        member.updateMemberBackgrounImg(s3Util.uploadImage(newImage, "member/background-image"));
    }

}
