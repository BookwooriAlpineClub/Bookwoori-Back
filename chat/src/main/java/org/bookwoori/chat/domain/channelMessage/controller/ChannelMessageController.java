package org.bookwoori.chat.domain.channelMessage.controller;

import lombok.RequiredArgsConstructor;
import org.bookwoori.chat.domain.channelMessage.dto.request.ChannelMessageDeleteRequestDto;
import org.bookwoori.chat.domain.channelMessage.dto.request.ChannelMessageModifyRequestDto;
import org.bookwoori.chat.domain.channelMessage.dto.request.ChannelMessageReactRequestDto;
import org.bookwoori.chat.domain.channelMessage.dto.request.ChannelMessageReplyRequestDto;
import org.bookwoori.chat.domain.channelMessage.dto.request.ChannelMessageSendRequestDto;
import org.bookwoori.chat.domain.channelMessage.dto.response.ChannelMessageListResponseDto;
import org.bookwoori.chat.domain.channelMessage.service.ChannelMessageService;
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
    public void send(@Payload ChannelMessageSendRequestDto requestDto,
        StompHeaderAccessor accessor) {
        messageSender.sendChannelMessage(requestDto,
            (Long) accessor.getSessionAttributes().get("memberId"));
    }

    @MessageMapping("/channel/react")
    public void react(@Payload ChannelMessageReactRequestDto requestDto,
        StompHeaderAccessor accessor) {
        messageSender.reactToChannelMessage(requestDto,
            (Long) accessor.getSessionAttributes().get("memberId"));
    }

    @MessageMapping("/channel/reply")
    public void reply(@Payload ChannelMessageReplyRequestDto requestDto,
        StompHeaderAccessor accessor) {
        messageSender.replyToChannelMessage(requestDto,
            (Long) accessor.getSessionAttributes().get("memberId"));
    }

    @MessageMapping("/channel/modify")
    public void modify(@Payload ChannelMessageModifyRequestDto requestDto,
        StompHeaderAccessor accessor) {
        messageSender.modifyChannelMessage(requestDto,
            (Long) accessor.getSessionAttributes().get("memberId"));
    }

    @MessageMapping("/channel/delete")
    public void delete(@Payload ChannelMessageDeleteRequestDto requestDto,
        StompHeaderAccessor accessor) {
        messageSender.deleteChannelMessage(requestDto,
            (Long) accessor.getSessionAttributes().get("memberId"));
    }

    @GetMapping
    public ResponseEntity<?> getChatHistory(
        @RequestParam(value = "channelId") final Long channelId,
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "100") int size) {
        ChannelMessageListResponseDto responseDto =
            channelMessageService.getChannelMessageList(channelId, page, size);
        return ResponseEntity.ok(responseDto);
    }
}
