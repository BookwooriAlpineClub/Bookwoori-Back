package org.bookwoori.core.domain.climbing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingChannelCreateRequestDto;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingChannelUpdateRequestDto;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingMemoUpdateRequestDto;
import org.bookwoori.core.domain.climbing.dto.request.ClimbingRoleDelegateRequestDto;
import org.bookwoori.core.domain.climbing.facade.ClimbingFacade;
import org.bookwoori.core.domain.climbing.facade.ClimbingMemberFacade;
import org.bookwoori.core.domain.reviewEmoji.entity.Emoji;
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
    private final ClimbingMemberFacade climbingMemberFacade;

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
    @PutMapping("/{climbingId}/members")
    public ResponseEntity<?> toggleParticipation(
        @PathVariable("climbingId") final Long climbingId) {
        boolean isJoined = climbingMemberFacade.toggleParticipation(climbingId);
        if (isJoined) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @Operation(summary = "클라이밍 채널 상세정보", description = "클라이밍 채널의 상세정보를 조회합니다.")
    @GetMapping("/{climbingId}")
    public ResponseEntity<?> getClimbingDetails(
        @PathVariable("climbingId") final Long climbingId) {
        return ResponseEntity.ok(climbingFacade.getClimbingDetails(climbingId));
    }

    @Operation(summary = "클라이밍 채널 권한 위임", description = "클라이밍 채널의 OWNER가 권한을 위임합니다.")
    @PatchMapping("/{climbingId}/members")
    public ResponseEntity<?> delegateClimbingRole(
        @PathVariable("climbingId") final Long climbingId,
        @RequestBody ClimbingRoleDelegateRequestDto requestDto) {
        climbingMemberFacade.delegateClimbingRole(climbingId, requestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "클라이밍 채널 참여자 조회", description = "클라이밍 채널 참여자 목록 / 참여자 독서 현황을 조회합니다.")
    @GetMapping("/{climbingId}/members")
    public ResponseEntity<?> getClimbingMembers(
        @PathVariable("climbingId") final Long climbingId) {
        return ResponseEntity.ok(climbingMemberFacade.getClimbingMembers(climbingId));
    }

    @Operation(summary = "클라이밍 채널 참여자 메모 수정", description = "클라이밍 채널 참여자의 메모를 수정합니다.")
    @PatchMapping("/{climbingId}/members/memo")
    public ResponseEntity<?> updateClimbingMemberMemo(
        @PathVariable("climbingId") final Long climbingId,
        @RequestBody @Valid ClimbingMemoUpdateRequestDto requestDto) {
        climbingMemberFacade.updateClimbingMemberMemo(climbingId, requestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "클라이밍 채널 참여자 감상평 공유", description = "클라이밍 채널 참여자가 감상평을 공유합니다")
    @PatchMapping("/{climbingId}/reviews")
    public ResponseEntity<?> shareReviewToClimbing(
        @PathVariable("climbingId") final Long climbingId) {
        climbingMemberFacade.shareReviewToClimbing(climbingId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "클라이밍 채널 참여자 감상평 리스트 조회", description = "클라이밍 채널 참여자의 감상평 리스트를 조회합니다.")
    @GetMapping("/{climbingId}/reviews")
    public ResponseEntity<?> getClimbingReviewList(
        @PathVariable("climbingId") final Long climbingId) {
        return ResponseEntity.ok(climbingMemberFacade.getClimbingReviewList(climbingId));
    }

    @Operation(summary = "클라이밍 채널 참여자 감상평 반응 추가 <-> 삭제", description = "클라이밍 채널 참여자가 공유한 감상평에 반응을 추가 <-> 삭제합니다.")
    @PutMapping("/{climbingId}/reviews/{reviewId}/emojis/{emoji}")
    public ResponseEntity<?> toggleReviewReaction(
        @PathVariable("climbingId") final Long climbingId,
        @PathVariable("reviewId") final Long reviewId,
        @PathVariable("emoji") final Emoji emoji) {
        boolean isCreated = climbingMemberFacade.toggleReviewReaction(climbingId, reviewId,
            emoji);
        if (isCreated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @Operation(summary = "클라이밍 채널 참여자 감상평 반응 리스트 조회", description = "클라이밍 채널 감상평 반응들의 멤버 리스트를 조회합니다.")
    @GetMapping("/{climbingId}/reviews/{reviewId}/emojis")
    public ResponseEntity<?> getEmojiMemberList(
        @PathVariable("climbingId") final Long climbingId,
        @PathVariable("reviewId") final Long reviewId) {
        return ResponseEntity.ok(
            climbingMemberFacade.getEmojiMemberList(reviewId));
    }
}
