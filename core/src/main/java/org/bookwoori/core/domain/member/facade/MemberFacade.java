package org.bookwoori.core.domain.member.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookwoori.core.domain.member.dto.request.UpdateMemberRequestDto;
import org.bookwoori.core.domain.member.dto.response.MemberResponseDto;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.member.service.MemberService;
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

    public void updateMember(UpdateMemberRequestDto requestDto) {
        Member currentMember = memberService.getCurrentMember();
        String profileImgUrl = updateImage(requestDto.profileImg(), currentMember.getProfileImg(),
            "member/profile-image");
        String backgroundImgUrl = updateImage(requestDto.backgroundImg(),
            currentMember.getBackgroundImg(), "member/background-image");
        currentMember.updateMember(requestDto.nickname(), profileImgUrl, backgroundImgUrl);
        memberService.saveMember(currentMember);
    }

    private String updateImage(MultipartFile newImage, String oldImageUrl, String path) {
        if (newImage != null && !newImage.isEmpty()) {
            if (oldImageUrl != null) {
                s3Util.deleteImage(oldImageUrl);
            }
            return s3Util.uploadImage(newImage, path);
        }
        return oldImageUrl;
    }

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
}
