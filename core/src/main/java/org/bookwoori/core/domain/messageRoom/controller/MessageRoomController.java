package org.bookwoori.core.domain.messageRoom.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.messageRoom.dto.request.MessageRoomCreateRequestDto;
import org.bookwoori.core.domain.messageRoom.facade.MessageRoomFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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


}
