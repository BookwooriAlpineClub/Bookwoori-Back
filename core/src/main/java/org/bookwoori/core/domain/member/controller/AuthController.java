package org.bookwoori.core.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookwoori.core.domain.member.dto.request.GetOrSaveMemberRequestDto;
import org.bookwoori.core.domain.member.facade.AuthFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "Auth")
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthFacade authFacade;
    
    @Operation(summary = "멤버 조회 또는 저장", description = "멤버 정보를 kakaoId로 조회하거나 새로운 멤버로 저장합니다.")
    @PostMapping("/members")
    public ResponseEntity<?> getOrSaveMember(
        @RequestBody @Valid GetOrSaveMemberRequestDto requestDto) {
        return ResponseEntity.ok(authFacade.getOrSaveMemberByKakaoId(
            requestDto));
    }

}
