package org.bookwoori.core.domain.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.climbing.facade.ClimbingFacade;
import org.bookwoori.core.domain.server.dto.request.ServerCreateRequestDto;
import org.bookwoori.core.domain.server.dto.request.ServerInfoUpdateRequestDto;
import org.bookwoori.core.domain.server.facade.ServerFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Tag(name = "Server")
@RequiredArgsConstructor
@RequestMapping("/servers")
public class ServerController {

    private final ServerFacade serverFacade;
    private final ClimbingFacade climbingFacade;

    @Operation(summary = "내 서버 목록 조회", description = "로그인한 유저가 참여한 서버 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<?> getServerList() {
        return ResponseEntity.ok(serverFacade.getServerList());
    }

    @Operation(summary = "모임 서버 생성", description = "모임 서버를 생성합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createServer(
        @Valid @ModelAttribute ServerCreateRequestDto requestDto) {
        serverFacade.createServer(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "서버 정보 조회", description = "특정 서버의 정보를 조회합니다.")
    @GetMapping("/{serverId}")
    public ResponseEntity<?> getServerDetails(@PathVariable final Long serverId) {
        return ResponseEntity.ok(serverFacade.getServerDetails(serverId));
    }

    @Operation(summary = "서버 정보 수정", description = "서버의 이름과 설명을 수정합니다.")
    @PatchMapping("/{serverId}")
    public ResponseEntity<?> updateServerInfo(@PathVariable final Long serverId,
        @Valid @RequestBody ServerInfoUpdateRequestDto requestDto) {
        serverFacade.updateServerInfo(serverId, requestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "서버 이미지 편집", description = "서버 이미지를 수정 또는 삭제합니다.")
    @PatchMapping(value = "/{serverId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateServerImage(@PathVariable final Long serverId,
        @RequestPart(required = false) MultipartFile imageFile) {
        serverFacade.updateServerImage(serverId, imageFile);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "서버 멤버 목록 조회", description = "해당 서버에 속한 멤버 목록을 조회합니다.")
    @GetMapping("/{serverId}/members")
    public ResponseEntity<?> getServerMemberList(@PathVariable final Long serverId) {
        return ResponseEntity.ok(serverFacade.getServerMemberList(serverId));
    }

    @Operation(summary = "서버 나가기", description = "로그인한 유저를 해당 서버에서 나가게 합니다.")
    @DeleteMapping("/{serverId}/members")
    public ResponseEntity<?> leaveServer(@PathVariable final Long serverId) {
        serverFacade.leaveServer(serverId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "채널 목록 조회", description = "해당 서버의 채널 및 카테고리 목록을 조회합니다.")
    @GetMapping("/{serverId}/categories")
    public ResponseEntity<?> getCategoryList(@PathVariable final Long serverId) {
        return ResponseEntity.ok(serverFacade.getServerCategoryList(serverId));
    }

    @Operation(summary = "클라이밍 채널 목록 조회", description = "클라이밍 채널의 목록을 조회합니다.")
    @GetMapping("/{serverId}/climbs")
    public ResponseEntity<?> getClimbingChannelList(
        @PathVariable final Long serverId) {
        return ResponseEntity.ok(climbingFacade.getClimbingList(serverId));
    }

    @Operation(summary = "내 클라이밍 채널 목록 조회", description = "내가 참여한 클라이밍 채널의 목록을 조회합니다.")
    @GetMapping("/{serverId}/climbs/me")
    public ResponseEntity<?> getMyClimbingChannelList(
        @PathVariable final Long serverId) {
        return ResponseEntity.ok(climbingFacade.getMyClimbingList(serverId));
    }

    @Operation(summary = "모집 중인 클라이밍 채널 목록 조회", description = "모집 중인 클라이밍 채널의 목록을 조회합니다.")
    @GetMapping("/{serverId}/climbs/ready")
    public ResponseEntity<?> getReadyClimbingChannelList(
        @PathVariable final Long serverId) {
        return ResponseEntity.ok(climbingFacade.getReadyClimbingList(serverId));
    }

    @Operation(summary = "초대코드 조회 또는 생성", description = "특정 서버에 대한 초대 코드를 조회 또는 생성합니다.")
    @PostMapping("/{serverId}/code")
    public ResponseEntity<?> getOrCreateInviteCode(@PathVariable final Long serverId) {
        return ResponseEntity.ok(serverFacade.getOrCreateInviteCode(serverId));
    }

    @Operation(summary = "초대 수락", description = "사용자를 초대코드에 해당하는 서버에 멤버로 추가합니다.")
    @PostMapping("/join/{inviteCode}")
    public ResponseEntity<?> createServerMember(@PathVariable String inviteCode) {
        serverFacade.createServerMember(inviteCode);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
