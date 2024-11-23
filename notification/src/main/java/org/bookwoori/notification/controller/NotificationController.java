package org.bookwoori.notification.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bookwoori.notification.dto.request.ChannelMessageRequest;
import org.bookwoori.notification.dto.request.DirectMessageRequest;
import org.bookwoori.notification.dto.response.CommonResponse;
import org.bookwoori.notification.service.NotificationService;
import org.bookwoori.notification.service.ResponseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/notification-server")
@RequiredArgsConstructor
public class NotificationController {

    private final ResponseService responseService;
    private final NotificationService notificationService;

    @PostMapping("/direct")
    public CommonResponse sendDirectMessage(@Valid @RequestBody DirectMessageRequest request) {
        log.info("POST /notification-server/direct / {}", request.getTarget());
        try {
            notificationService.send(request);
        } catch (Exception e) {
            log.error("NOTIFICATION ERROR - DM");
            e.printStackTrace();
        }
        return responseService.getSuccessResponse();
    }

    @PostMapping("/channel")
    public CommonResponse sendChannelMessage(@Valid @RequestBody ChannelMessageRequest request) {
        log.info("POST /notification-server/channel / {}", request.getTarget());
        try {
            notificationService.send(request);
        } catch (Exception e) {
            log.error("NOTIFICATION ERROR - CHANNEL");
            e.printStackTrace();
        }
        return responseService.getSuccessResponse();
    }
}
