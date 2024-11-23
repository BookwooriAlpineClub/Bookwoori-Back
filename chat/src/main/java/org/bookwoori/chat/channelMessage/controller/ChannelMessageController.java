package org.bookwoori.chat.channelMessage.controller;

import lombok.RequiredArgsConstructor;
import org.bookwoori.chat.channelMessage.dto.request.ChannelMessageSendRequestDto;
import org.bookwoori.chat.channelMessage.service.ChannelMessageService;
import org.bookwoori.chat.global.kafka.MessageSender;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
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
}
