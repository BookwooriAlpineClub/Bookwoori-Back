package org.bookwoori.notification.dto.request;

import java.time.LocalDateTime;


public record BatchChannelMessageRequestDto(
        LocalDateTime time,
        ChannelMessageRequestDto request

) {

}
