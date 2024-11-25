package org.bookwoori.chat.directMessage.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bookwoori.chat.directMessage.dto.request.DirectMessageReactRequestDto;
import org.bookwoori.chat.directMessage.dto.request.DirectMessageReplyRequestDto;
import org.bookwoori.chat.directMessage.dto.request.DirectMessageSendRequestDto;
import org.bookwoori.chat.directMessage.dto.response.DirectMessageListResponseDto;
import org.bookwoori.chat.directMessage.service.DirectMessageService;
import org.bookwoori.chat.global.kafka.MessageSender;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/directMessages")
public class DirectMessageController {

    private final MessageSender messageSender;
    private final DirectMessageService directMessageService;

    @MessageMapping("/direct/send")
    public void sendDirectMessage(@Payload DirectMessageSendRequestDto requestDto) {
        messageSender.sendDirectMessage(requestDto);
    }

    @MessageMapping("/direct/react")
    public void reactToDirectMessage(@Payload DirectMessageReactRequestDto requestDto) {
        messageSender.reactToDirectMessage(requestDto);
    }

    @MessageMapping("/direct/reply")
    public void replyToDirectMessage(@Payload DirectMessageReplyRequestDto requestDto) {
        messageSender.replyToDirectMessage(requestDto);
    }

    @GetMapping
    public ResponseEntity<?> getDirectMessageHistory(
        @RequestParam(value = "messageRoomId") final Long roomId,
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "100") int size) {
        DirectMessageListResponseDto responseDto =
            directMessageService.getDirectMessageHistory(roomId, page, size);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/recent")
    public ResponseEntity<?> getRecentMessageFromMessageRoom(
        @RequestParam("messageRoomIdList") List<Long> messageRoomIdList) {
        return ResponseEntity.ok(
            directMessageService.getRecentMessageFromMessageRoom(messageRoomIdList));
    }
}
