package org.bookwoori.chat.global.feignClient;

import org.bookwoori.chat.global.feignClient.dto.MessageNotificationRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "noti", url = "${client.url.notification}")
public interface NotificationClient {

    @PostMapping("notification-server/direct")
    void sendDirectNotification(MessageNotificationRequestDto requestDto);

    @PostMapping("notification-server/chat")
    void sendChannelNotification(MessageNotificationRequestDto requestDto);
}
