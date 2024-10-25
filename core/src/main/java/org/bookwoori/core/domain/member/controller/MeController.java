package org.bookwoori.core.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.member.dto.request.UpdateMemberRequestDto;
import org.bookwoori.core.domain.member.service.MeService;
import org.bookwoori.core.domain.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Me")
@RequiredArgsConstructor
@RequestMapping("/me")
public class MeController {
    private final MeService meService;

    @Operation(summary = "프로필 수정", description = "닉네임, 프로필 이미지를 수정합니다.")
    @PatchMapping("/update")
    public ResponseEntity<?> updateMember(@RequestBody @Valid UpdateMemberRequestDto requestDto) {
        meService.updateMember(requestDto);
        return ResponseEntity.ok().build();
    }

}
