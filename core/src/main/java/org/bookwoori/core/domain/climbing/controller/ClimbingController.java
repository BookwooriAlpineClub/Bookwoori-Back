package org.bookwoori.core.domain.climbing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.channel.dto.request.ChannelCreateRequestDto;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingChannelCreateRequestDto;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingChannelUpdateRequestDto;
import org.bookwoori.core.domain.climbing.entity.Climbing;
import org.bookwoori.core.domain.climbing.facade.ClimbingFacade;
import org.bookwoori.core.domain.climbing.service.ClimbingService;
import org.bookwoori.core.domain.member.dto.request.UpdateMemberRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Climbing")
@RequiredArgsConstructor
@RequestMapping("/climbs")
public class ClimbingController {
    private final ClimbingFacade climbingFacade;

    @Operation(summary = "클라이밍 채널 생성", description = "클라이밍 채널을 생성합니다.")
    @PostMapping
    public ResponseEntity<?> createClimbingChannel(@RequestBody @Valid ClimbingChannelCreateRequestDto requestDto) {
        climbingFacade.createClimbing(requestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "클라이밍 채널 편집", description = "클라이밍 채널을 편집합니다.")
    @PatchMapping
    public ResponseEntity<?> updateClimbingChannel(@RequestBody @Valid ClimbingChannelUpdateRequestDto requestDto) {
        climbingFacade.updateClimbing(requestDto);
        return ResponseEntity.ok().build();
    }

}
