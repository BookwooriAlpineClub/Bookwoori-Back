package org.bookwoori.chat.domain.directMessage.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bookwoori.chat.domain.directMessage.dto.request.DirectMessageDeleteRequestDto;
import org.bookwoori.chat.domain.directMessage.dto.request.DirectMessageModifyRequestDto;
import org.bookwoori.chat.domain.directMessage.dto.request.DirectMessageReactRequestDto;
import org.bookwoori.chat.domain.directMessage.dto.request.DirectMessageReplyRequestDto;
import org.bookwoori.chat.domain.directMessage.dto.request.DirectMessageSendRequestDto;
import org.bookwoori.chat.domain.directMessage.dto.response.DirectMessageListResponseDto;
import org.bookwoori.chat.domain.directMessage.service.DirectMessageService;
import org.bookwoori.chat.global.kafka.MessageSender;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
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
    public void sendDirectMessage(@Payload DirectMessageSendRequestDto requestDto,
        StompHeaderAccessor accessor) {
        messageSender.sendDirectMessage(requestDto,
            (Long) accessor.getSessionAttributes().get("memberId"));
    }

    @MessageMapping("/direct/react")
    public void reactToDirectMessage(@Payload DirectMessageReactRequestDto requestDto,
        StompHeaderAccessor accessor) {
        messageSender.reactToDirectMessage(requestDto,
            (Long) accessor.getSessionAttributes().get("memberId"));
    }

    @MessageMapping("/direct/reply")
    public void replyToDirectMessage(@Payload DirectMessageReplyRequestDto requestDto,
        StompHeaderAccessor accessor) {
        messageSender.replyToDirectMessage(requestDto,
            (Long) accessor.getSessionAttributes().get("memberId"));
    }

    @MessageMapping("/direct/modify")
    public void modifyDirectMessage(@Payload DirectMessageModifyRequestDto requestDto,
        StompHeaderAccessor accessor) {
        messageSender.modifyDirectMessage(requestDto,
            (Long) accessor.getSessionAttributes().get("memberId"));
    }

    @MessageMapping("/direct/delete")
    public void deleteDirectMessage(@Payload DirectMessageDeleteRequestDto requestDto,
        StompHeaderAccessor accessor) {
        messageSender.deleteDirectMessage(requestDto,
            (Long) accessor.getSessionAttributes().get("memberId"));
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
