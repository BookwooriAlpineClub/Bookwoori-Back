package org.bookwoori.core.domain.member.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookwoori.core.domain.member.dto.response.LoginResponse;
import org.bookwoori.core.domain.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "Auth")
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final MemberService memberService;

    @GetMapping("/success")
    public ResponseEntity<?> loginSuccess(@Valid LoginResponse loginResponse) {
        return ResponseEntity.ok(loginResponse);
    }

}
