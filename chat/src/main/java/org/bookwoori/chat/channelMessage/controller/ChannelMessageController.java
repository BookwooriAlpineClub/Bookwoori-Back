package org.bookwoori.chat.channelMessage.controller;

import lombok.RequiredArgsConstructor;
import org.bookwoori.chat.channelMessage.dto.request.ChannelMessageReactRequestDto;
import org.bookwoori.chat.channelMessage.dto.request.ChannelMessageSendRequestDto;
import org.bookwoori.chat.channelMessage.dto.response.ChannelMessageListResponseDto;
import org.bookwoori.chat.channelMessage.service.ChannelMessageService;
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
@RequestMapping("/channelMessages")
public class ChannelMessageController {

    private final MessageSender messageSender;
    private final ChannelMessageService channelMessageService;

    @MessageMapping("/channel/send")
    public void sendChannelMessage(@Payload ChannelMessageSendRequestDto requestDto,
        StompHeaderAccessor accessor) {
        messageSender.sendChannelMessage(requestDto,
            (Long) accessor.getSessionAttributes().get("memberId"));
    }

    @MessageMapping("/channel/react")
    public void reactToDirectMessage(@Payload ChannelMessageReactRequestDto requestDto,
        StompHeaderAccessor accessor) {
        messageSender.reactToChannelMessage(requestDto,
            (Long) accessor.getSessionAttributes().get("memberId"));
    }

    @GetMapping
    public ResponseEntity<?> getChannelMessageHistory(
        @RequestParam(value = "channelId") final Long channelId,
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "100") int size) {
        ChannelMessageListResponseDto responseDto =
            channelMessageService.getChannelMessageHistory(channelId, page, size);
        return ResponseEntity.ok(responseDto);
    }
}
