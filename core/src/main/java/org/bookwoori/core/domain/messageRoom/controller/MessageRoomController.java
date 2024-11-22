package org.bookwoori.core.domain.messageRoom.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.messageRoom.dto.request.MessageRoomCreateRequestDto;
import org.bookwoori.core.domain.messageRoom.facade.MessageRoomFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "MessageRoom")
@RequiredArgsConstructor
@RequestMapping("/messageRooms")
public class MessageRoomController {

    private final MessageRoomFacade messageRoomFacade;

    @Operation(summary = "DM 방 정보 조회", description = "특정 유저와 로그인 유저 사이에 생성된 DM 방 정보를 조회합니다. 만약 두 유저 사이에 DM 방이 존재하지 않는다면 새로 생성합니다.")
    @PostMapping
    public ResponseEntity<?> getOrCreateMessageRoom(
        @RequestBody @Valid MessageRoomCreateRequestDto requestDto) {
        return ResponseEntity.ok(messageRoomFacade.getOrCreateMessageRoom(requestDto));
    }

    @Operation(summary = "내 DM 방 목록 조회", description = "로그인한 유저의 DM 방 목록을 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<?> getMyMessageRoomList(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(messageRoomFacade.getMyMessageRoomList(page, size));
    }

    @GetMapping("/{messageRoomId}/members")
    public ResponseEntity<?> getMembersProfile(@PathVariable final Long messageRoomId) {
        return ResponseEntity.ok(messageRoomFacade.getMembersProfile(messageRoomId));
    }

}
