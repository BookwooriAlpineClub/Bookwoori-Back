package org.bookwoori.core.domain.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.server.dto.request.ServerCreateRequestDto;
import org.bookwoori.core.domain.server.facade.ServerFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Server")
@RequiredArgsConstructor
@RequestMapping("/servers")
public class ServerController {

    private final ServerFacade serverFacade;

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

    @Operation(summary = "서버 멤버 목록 조회", description = "해당 서버에 속한 멤버 목록을 조회합니다.")
    @GetMapping("/{serverId}/members")
    public ResponseEntity<?> getServerMemberList(@PathVariable final Long serverId) {
        return ResponseEntity.ok(serverFacade.getServerMemberList(serverId));
    }

    @Operation(summary = "채널 목록 조회", description = "해당 서버의 채널 및 카테고리 목록을 조회합니다.")
    @GetMapping("/{serverId}/categories")
    public ResponseEntity<?> getCategoryList(@PathVariable final Long serverId) {
        return ResponseEntity.ok(serverFacade.getServerCategoryList(serverId));
    }

}
