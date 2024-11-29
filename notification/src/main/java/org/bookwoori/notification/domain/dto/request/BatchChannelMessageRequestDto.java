package org.bookwoori.notification.domain.dto.request;

import java.time.LocalDateTime;


public record BatchChannelMessageRequestDto(
        LocalDateTime time,
        ChannelMessageRequestDto request

) {

}
