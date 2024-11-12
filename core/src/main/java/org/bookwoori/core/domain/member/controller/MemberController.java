package org.bookwoori.core.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookwoori.core.domain.member.dto.request.UpdateMemberRequestDto;
import org.bookwoori.core.domain.member.facade.MemberFacade;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "Member")
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberFacade memberFacade;

    @Operation(summary = "프로필 수정", description = "닉네임, 프로필 이미지를 수정합니다.")
    @PatchMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateMember(
        @ModelAttribute @Valid UpdateMemberRequestDto requestDto) {
        memberFacade.updateMember(requestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "유저 서재 조회", description = "유저의 서재를 조회합니다.")
    @GetMapping(value = "/{memberId}/library")
    public ResponseEntity<?> getMemberLibrary(@PathVariable("memberId") final Long memberId) {
        return ResponseEntity.ok(memberFacade.getMemberLibrary(memberId));
    }

    @Operation(summary = "유저 프로필 조회", description = "유저의 프로필을 조회합니다.")
    @GetMapping(value = "/{memberId}")
    public ResponseEntity<?> getMemberProfile(@PathVariable("memberId") final Long memberId) {
        return ResponseEntity.ok(memberFacade.getMemberProfile(memberId));
    }
}
