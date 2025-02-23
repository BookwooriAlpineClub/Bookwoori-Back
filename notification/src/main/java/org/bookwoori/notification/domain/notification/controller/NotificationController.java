package org.bookwoori.notification.domain.notification.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookwoori.notification.domain.notification.service.NotificationService;
import org.bookwoori.notification.domain.notification.dto.request.ChannelMessageRequestDto;
import org.bookwoori.notification.domain.notification.dto.request.ChatMessageRequestDto;
import org.bookwoori.notification.domain.notification.dto.request.DirectMessageRequestDto;
import org.bookwoori.notification.domain.notification.dto.request.EmojiMessageRequestDto;
import org.bookwoori.notification.domain.notification.dto.response.CommonResponseDto;
import org.bookwoori.notification.domain.device.service.ResponseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@Tag(name = "Notification")
@RequestMapping("/notification-server")
@RequiredArgsConstructor
public class NotificationController {

    private final ResponseService responseService;
    private final NotificationService notificationService;

    @Operation(summary = "DM 알림", description = "DM 알림을 보냅니다.")
    @PostMapping("/direct")
    public CommonResponseDto sendDirectMessage(@Valid @RequestBody DirectMessageRequestDto request) {
        try {
            notificationService.send(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseService.getSuccessResponse();
    }

    @Operation(summary = "채널 생성 알림", description = "채널 생성 알림을 보냅니다.")
    @PostMapping("/channel")
    public CommonResponseDto sendChannelMessage(@Valid @RequestBody ChannelMessageRequestDto request) {
        try {
            notificationService.send(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseService.getSuccessResponse();
    }

    @Operation(summary = "감상평 이모지 알림", description = "감상평 이모지 알림을 보냅니다.")
    @PostMapping("/emoji")
    public CommonResponseDto sendEmojiMessage(@Valid @RequestBody EmojiMessageRequestDto request) {
        try {
            notificationService.send(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseService.getSuccessResponse();
    }

    @Operation(summary = "채팅 채널 알림", description = "채팅 채널에 달린 채팅에 대한 알림을 보냅니다.")
    @PostMapping("/chat")
    public CommonResponseDto sendChatMessage(@Valid @RequestBody ChatMessageRequestDto request) {
        try {
            notificationService.send(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseService.getSuccessResponse();
    }


}
