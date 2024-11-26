package org.bookwoori.notification.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatchChannelMessageRequestDto {
    private LocalDateTime time;
    private ChannelMessageRequestDto request;
}
