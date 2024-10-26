package org.bookwoori.core.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookwoori.core.domain.member.dto.request.UpdateMemberRequestDto;
import org.bookwoori.core.domain.member.facade.MeFacade;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Tag(name = "Me")
@RequiredArgsConstructor
@RequestMapping("/me")
public class MeController {
    private final MeFacade meFacade;

    @Operation(summary = "프로필 수정", description = "닉네임, 프로필 이미지를 수정합니다.")
    @PatchMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateMember(@ModelAttribute @Valid UpdateMemberRequestDto requestDto) {
        meFacade.updateMember(requestDto);
        return ResponseEntity.ok().build();
    }
}
