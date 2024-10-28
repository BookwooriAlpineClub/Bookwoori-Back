package org.bookwoori.core.domain.climbing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingChannelCreateRequestDto;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingChannelUpdateRequestDto;
import org.bookwoori.core.domain.climbing.facade.ClimbingFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Climbing")
@RequiredArgsConstructor
@RequestMapping("/climbs")
public class ClimbingController {

    private final ClimbingFacade climbingFacade;

    @Operation(summary = "클라이밍 채널 생성", description = "클라이밍 채널을 생성합니다.")
    @PostMapping
    public ResponseEntity<?> createClimbingChannel(
        @RequestBody @Valid ClimbingChannelCreateRequestDto requestDto) {
        climbingFacade.createClimbing(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "클라이밍 채널 편집", description = "클라이밍 채널을 편집합니다.")
    @PatchMapping("/{climbingId}")
    public ResponseEntity<?> updateClimbingChannel(
        @PathVariable("climbingId") final Long climbingId,
        @RequestBody @Valid ClimbingChannelUpdateRequestDto requestDto) {
        climbingFacade.updateClimbing(climbingId, requestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "클라이밍 채널 참여 <-> 참여 취소", description = "클라이밍 채널에 참여 <-> 참여를 취소합니다.")
    @PutMapping("/{climbingId}")
    public ResponseEntity<?> toggleClimbingChannel(
        @PathVariable("climbingId") final Long climbingId) {
        boolean isJoined = climbingFacade.toggleClimbing(climbingId);
        if (isJoined) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @Operation(summary = "클라이밍 채널 상세정보", description = "클라이밍 채널의 상세정보를 조회합니다.")
    @GetMapping("/{climbingId}")
    public ResponseEntity<?> getClimbingChannel(
        @PathVariable("climbingId") final Long climbingId) {
        return ResponseEntity.ok(climbingFacade.getClimbing(climbingId));
    }

}
