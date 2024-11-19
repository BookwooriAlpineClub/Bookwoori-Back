package org.bookwoori.chat.directMessage.controller;

import lombok.RequiredArgsConstructor;
import org.bookwoori.chat.directMessage.dto.request.DirectMessageSendRequestDto;
import org.bookwoori.chat.global.kafka.MessageSender;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DirectMessageController {

    private final MessageSender messageSender;

    @MessageMapping("/direct/send")
    public void sendDirectMessage(@Payload DirectMessageSendRequestDto requestDto) {
        messageSender.sendDirectMessage(requestDto);
    }
}
