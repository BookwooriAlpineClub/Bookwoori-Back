package org.bookwoori.chat.channelMessage.controller;

import lombok.RequiredArgsConstructor;
import org.bookwoori.chat.channelMessage.dto.request.ChannelMessageSendRequestDto;
import org.bookwoori.chat.channelMessage.dto.response.ChannelMessageListResponseDto;
import org.bookwoori.chat.channelMessage.service.ChannelMessageService;
import org.bookwoori.chat.global.kafka.MessageSender;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChannelMessageController {

    private final MessageSender messageSender;
    private final ChannelMessageService channelMessageService;

    @MessageMapping("/channel/send")
    public void sendChannelMessage(@Payload ChannelMessageSendRequestDto requestDto) {
        messageSender.sendChannelMessage(requestDto);
    }

    @GetMapping("/channels/{channelId}/messages")
    public ResponseEntity<?> getChannelMessageHistory(
        @PathVariable(value = "channelId") final Long channelId,
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "100") int size) {
        ChannelMessageListResponseDto responseDto =
            channelMessageService.getChannelMessageHistory(channelId, page, size);
        return ResponseEntity.ok(responseDto);
    }
}
