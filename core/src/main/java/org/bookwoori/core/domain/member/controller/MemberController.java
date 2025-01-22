package org.bookwoori.core.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookwoori.core.domain.member.dto.request.UpdateMemberRequestDto;
import org.bookwoori.core.domain.member.facade.AuthFacade;
import org.bookwoori.core.domain.member.facade.MemberFacade;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@Tag(name = "Member")
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberFacade memberFacade;
    private final AuthFacade authFacade;

    @Operation(summary = "프로필 수정", description = "유저의 프로필을 수정합니다.")
    @PatchMapping(value = "/me")
    public ResponseEntity<?> updateMember(
        @RequestBody @Valid UpdateMemberRequestDto requestDto) {
        memberFacade.update(requestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "프로필 이미지 편집", description = "유저의 프로필 이미지를 수정합니다.")
    @PatchMapping(value = "/me/profileImg", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateMemberProfileImg(
        @RequestPart(required = false) MultipartFile imageFile) {
        memberFacade.updateProfileImg(imageFile);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "프로필 배경 이미지 편집", description = "유저의 프로필 배경 이미지를 수정합니다.")
    @PatchMapping(value = "/me/backgroundImg", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateMemberBackgroundImg(
        @RequestPart(required = false) MultipartFile imageFile) {
        memberFacade.updateBackgroundImg(imageFile);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "유저 조회", description = "유저의 서재 및 프로필을 조회합니다.")
    @GetMapping(value = "/{memberId}")
    public ResponseEntity<?> getMemberProfile(@PathVariable("memberId") final Long memberId) {
        return ResponseEntity.ok(memberFacade.getMemberProfile(memberId));
    }

    @Operation(summary = "내 프로필 조회", description = "자신의 서재 및 프로필을 조회합니다.")
    @GetMapping(value = "/me")
    public ResponseEntity<?> getMyProfile() {
        return ResponseEntity.ok(memberFacade.getMyProfile());
    }

    @Operation(summary = "계정 삭제", description = "회원 상태를 INACTIVE로 변경하고 닉네임을 '(알 수 없음)'으로 변경합니다.")
    @PatchMapping("/delete")
    public ResponseEntity<?> deleteMember() {
        authFacade.deleteMember();
        return ResponseEntity.ok().build();
    }
}
